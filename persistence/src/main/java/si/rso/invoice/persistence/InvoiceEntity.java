package si.rso.invoice.persistence;

import javax.persistence.*;

@Entity
@Table(name = "invoices")
@NamedQueries(
        @NamedQuery(name = InvoiceEntity.FIND_BY_ORDER_ID, query = "SELECT i FROM InvoiceEntity i WHERE i.orderId = :orderId")
)
public class InvoiceEntity extends BaseEntity {

    public static final String FIND_BY_ORDER_ID = "InvoiceEntity.findByOrderId";
    
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
