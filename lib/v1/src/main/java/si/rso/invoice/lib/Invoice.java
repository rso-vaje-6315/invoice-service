package si.rso.invoice.lib;

import java.util.List;

public class Invoice extends BaseType {
    
    private List<InvoiceItem> items;
    
    public List<InvoiceItem> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItem> items) {
        this.items = items;
    }
}
