package si.rso.invoice.services.impl;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.rso.invoice.lib.Invoice;
import si.rso.invoice.mappers.InvoiceMapper;
import si.rso.invoice.persistence.InvoiceEntity;
import si.rso.invoice.services.InvoiceService;
import si.rso.rest.exceptions.NotFoundException;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class InvoiceServiceImpl implements InvoiceService {
    
    @PersistenceContext(unitName = "main-jpa-unit")
    private EntityManager em;
    
    @Override
    public void createInvoice(String orderId) {
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
}
