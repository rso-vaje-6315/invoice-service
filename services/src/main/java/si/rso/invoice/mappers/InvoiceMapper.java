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
        
        if (entity.getItems() != null) {
            List<InvoiceItem> items = entity.getItems()
                .stream()
                .map(InvoiceMapper::fromInvoiceItemEntity)
                .collect(Collectors.toList());
            invoice.setItems(items);
        } else {
            invoice.setItems(new ArrayList<>());
        }
        
        return invoice;
    }
    
    public static InvoiceItem fromInvoiceItemEntity(InvoiceItemEntity entity) {
        InvoiceItem item = new InvoiceItem();
        
        item.setId(entity.getId());
        item.setTimestamp(entity.getTimestamp());
        
        return item;
    }
    
}
