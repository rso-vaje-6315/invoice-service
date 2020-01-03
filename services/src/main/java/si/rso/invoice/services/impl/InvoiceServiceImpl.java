package si.rso.invoice.services.impl;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.lowagie.text.DocumentException;
import org.xhtmlrenderer.pdf.ITextRenderer;
import si.rso.invoice.apis.OrdersApi;
import si.rso.invoice.lib.Invoice;
import si.rso.invoice.mappers.InvoiceMapper;
import si.rso.invoice.persistence.InvoiceConfigEntity;
import si.rso.invoice.persistence.InvoiceEntity;
import si.rso.invoice.persistence.InvoiceItemEntity;
import si.rso.invoice.services.InvoiceService;
import si.rso.invoice.services.NotificationService;
import si.rso.invoice.services.StorageConnection;
import si.rso.invoice.services.TemplateService;
import si.rso.orders.lib.Order;
import si.rso.rest.exceptions.NotFoundException;
import si.rso.rest.exceptions.RestException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// import si.rso.invoice.services.OrderService;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {
    
    @PersistenceContext(unitName = "main-jpa-unit")
    private EntityManager em;
    
    @Inject
    private TemplateService templateService;
    
    @Inject
    private NotificationService notificationService;
    
    @Inject
    private StorageConnection storageConnection;
    
    private OrdersApi ordersApi;
    
    @Override
    @Transactional
    public Invoice createInvoice(String orderId) {
        
        // Order order = ordersApi.getOrder(orderId);
        
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setCustomerId("order.getCustomerId()");
        invoiceEntity.setItems(new ArrayList<>());
    
        InvoiceItemEntity item1 = new InvoiceItemEntity();
        item1.setCode("12334");
        item1.setName("Hay bales");
        item1.setQuantity(12);
        item1.setPrice(34.12);
        item1.setTotalPrice(409.44);
        invoiceEntity.getItems().add(item1);
    
        InvoiceItemEntity item2 = new InvoiceItemEntity();
        item2.setCode("45211");
        item2.setName("Pitchforks");
        item2.setQuantity(1);
        item2.setPrice(29);
        item2.setTotalPrice(29);
        invoiceEntity.getItems().add(item2);
    
        InvoiceItemEntity item3 = new InvoiceItemEntity();
        item3.setCode("09821");
        item3.setName("Pigs");
        item3.setQuantity(3);
        item3.setPrice(199);
        item3.setTotalPrice(597);
        invoiceEntity.getItems().add(item3);
        em.persist(invoiceEntity);
        
        String invoiceUrl = this.generatePrintableInvoice(invoiceEntity);
        invoiceEntity.setInvoiceUrl(invoiceUrl);
        em.merge(invoiceEntity);
    
        notificationService.sendNotification(invoiceEntity);
        
        return InvoiceMapper.fromInvoiceEntity(invoiceEntity);
    }
    
    @Override
    public List<Invoice> getInvoices(QueryParameters query) {
        return JPAUtils.queryEntities(em, InvoiceEntity.class, query)
            .stream()
            .map(InvoiceMapper::fromInvoiceEntity)
            .collect(Collectors.toList());
    }
    
    @Override
    public long getInvoicesCount(QueryParameters query) {
        return JPAUtils.queryEntitiesCount(em, InvoiceEntity.class, query);
    }
    
    @Override
    public Invoice getInvoice(String invoiceId) {
        InvoiceEntity invoiceEntity = em.find(InvoiceEntity.class, invoiceId);
        if (invoiceEntity == null) {
            throw new NotFoundException(InvoiceEntity.class, invoiceId);
        }
        return InvoiceMapper.fromInvoiceEntity(invoiceEntity);
    }
    
    private String generatePrintableInvoice(InvoiceEntity invoice) {
        
        Map<String, Object> params = this.generateInvoiceItems(invoice);
    
        String renderedContent = this.templateService.renderTemplate("invoice", params);
        
        FileOutputStream fileOutputStream = null;
        String filename = "invoice-order-" + invoice.getId() + "-" + invoice.getCustomerId();
        try {
            final File pdfFile = File.createTempFile(filename, ".pdf");
            fileOutputStream = new FileOutputStream(pdfFile);
    
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(renderedContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();
    
            if (!Files.exists(Path.of("invoices"))) {
                Files.createDirectory(Path.of("invoices"));
            }
            Path storedFilePath = Files.copy(pdfFile.toPath(), Path.of("invoices/" + filename + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
            
            return storageConnection.uploadFile(new File(storedFilePath.toString()));
            
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            throw new RestException("Error generating pdf!");
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private double getVatRate() {
        try {
            String vat = this.getInvoiceConfig("vat.rate");
            return Double.parseDouble(vat);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new RestException("Error parsing VAT rate!");
        }
    }
    
    private String getInvoiceConfig(String key) {
        TypedQuery<InvoiceConfigEntity> query = em.createNamedQuery(InvoiceConfigEntity.FIND_BY_CODE, InvoiceConfigEntity.class);
        query.setParameter("code", key);
        try {
            InvoiceConfigEntity configEntity = query.getSingleResult();
            return configEntity.getValue();
        } catch (NoResultException | NonUniqueResultException e) {
            e.printStackTrace();
            throw new RestException("Error retrieving invoice config '" + key + "'!");
        }
    }
    
    private Map<String, Object> generateInvoiceItems(InvoiceEntity invoice) {
        Map<String, Object> params = new HashMap<>();
        
        double vatRate = this.getVatRate();
        params.put("vatRate", vatRate);
        params.put("items", invoice.getItems());
        double totalPrice = invoice.getItems().stream().mapToDouble(InvoiceItemEntity::getTotalPrice).sum();
        params.put("totalPrice", totalPrice);
        double taxPrice = totalPrice * vatRate;
        params.put("taxPrice", taxPrice);
        params.put("preTax", (totalPrice - taxPrice));
        
        params.put("invoiceId", invoice.getId());
        
        readValuesFromDB(params);
        
        return params;
    }
    
    private void readValuesFromDB(Map<String, Object> params) {
        String[] configKeys = {
            "seller.name",
            "seller.street",
            "seller.street.num",
            "seller.post",
            "seller.post.code",
            "seller.country",
            "seller.phone",
            "seller.tax.num",
            "seller.bic",
            "seller.iban",
            "seller.email"
        };
        for (String key : configKeys) {
            String configValue = this.getInvoiceConfig(key);
            params.put(key.replaceAll("\\.", ""), configValue);
        }
    }
}
