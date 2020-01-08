package si.rso.invoice.mappers;

import si.rso.invoice.lib.Invoice;
import si.rso.invoice.lib.InvoiceItem;
import si.rso.invoice.persistence.InvoiceEntity;
import si.rso.invoice.persistence.InvoiceItemEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceMapper {
    
    public static Invoice fromInvoiceEntity(InvoiceEntity entity) {
        Invoice invoice = new Invoice();
        
        invoice.setId(entity.getId());
        invoice.setTimestamp(entity.getTimestamp());
        
        invoice.setCustomerId(entity.getCustomerId());
        invoice.setOrderId(entity.getOrderId());
        invoice.setInvoiceUrl(entity.getInvoiceUrl());
        
        return invoice;
    }
    
}
