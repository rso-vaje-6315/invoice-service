package si.rso.invoice.persistence;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "invoices")
public class InvoiceEntity extends BaseEntity {
    
    private String customerId;
    
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<InvoiceItemEntity> items;
    
    @Column(name = "invoice_url")
    private String invoiceUrl;
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public List<InvoiceItemEntity> getItems() {
        return items;
    }
    
    public void setItems(List<InvoiceItemEntity> items) {
        this.items = items;
    }
    
    public String getInvoiceUrl() {
        return invoiceUrl;
    }
    
    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }
}
