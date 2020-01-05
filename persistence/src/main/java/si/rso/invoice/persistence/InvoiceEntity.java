package si.rso.invoice.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "invoices")
public class InvoiceEntity extends BaseEntity {
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "invoice_url")
    private String invoiceUrl;
    
    @Column(name = "order_id")
    private String orderId;
    
    public String getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    
    public String getInvoiceUrl() {
        return invoiceUrl;
    }
    
    public void setInvoiceUrl(String invoiceUrl) {
        this.invoiceUrl = invoiceUrl;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
