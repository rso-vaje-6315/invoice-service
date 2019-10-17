package si.rso.invoice.services.impl;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.lowagie.text.DocumentException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.xhtmlrenderer.pdf.ITextRenderer;
import si.rso.invoice.lib.Invoice;
import si.rso.invoice.mappers.InvoiceMapper;
import si.rso.invoice.persistence.InvoiceConfigEntity;
import si.rso.invoice.persistence.InvoiceEntity;
import si.rso.invoice.persistence.InvoiceItemEntity;
import si.rso.invoice.services.InvoiceService;
import si.rso.invoice.services.OrderService;
import si.rso.invoice.services.TemplateService;
import si.rso.rest.exceptions.NotFoundException;
import si.rso.rest.exceptions.RestException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {
    
    @PersistenceContext(unitName = "main-jpa-unit")
    private EntityManager em;
    
    @Inject
    private TemplateService templateService;
    
    /*@Inject
    @RestClient
    private OrderService orderService;*/
    
    @Override
    public void createInvoice(String orderId) {
        
        //TODO: import order-service-lib and replace entities
        //Object order = orderService.getOrder(orderId);
        
        InvoiceEntity invoiceEntity = new InvoiceEntity();
        invoiceEntity.setId("invoiceId");
        invoiceEntity.setCustomerId("customerId");
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
        
        this.generatePrintableInvoice(invoiceEntity);
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
    
    private void generatePrintableInvoice(InvoiceEntity invoice) {
        
        Map<String, Object> params = this.generateInvoiceItems(invoice);
    
        String renderedContent = this.templateService.renderTemplate("invoice", params);
    
        System.err.println(renderedContent);
        
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
            Files.copy(pdfFile.toPath(), Path.of("invoices/" + filename + ".pdf"), StandardCopyOption.REPLACE_EXISTING);
            
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
        TypedQuery<InvoiceConfigEntity> query = em.createNamedQuery(InvoiceConfigEntity.FIND_BY_CODE, InvoiceConfigEntity.class);
        query.setParameter("code", "vat.rate");
        try {
            InvoiceConfigEntity configEntity = query.getSingleResult();
            return Double.parseDouble(configEntity.getValue());
        } catch (NoResultException | NonUniqueResultException | NumberFormatException e) {
            e.printStackTrace();
            throw new RestException("Error retrieving VAT rate!");
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
        
        return params;
    }
}
