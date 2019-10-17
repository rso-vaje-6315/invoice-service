package si.rso.invoice.persistence;

import javax.persistence.*;

@Entity
@Table(name = "invoice_items")
public class InvoiceItemEntity extends BaseEntity {
    
    private String code;
    
    private String name;
    
    private double price;
    
    private double quantity;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getQuantity() {
        return quantity;
    }
    
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
