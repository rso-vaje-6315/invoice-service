package si.rso.invoice.lib;

import java.util.List;

public class Invoice extends BaseType {
    
    private String customerId;
    
    private List<InvoiceItem> items;
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
}
