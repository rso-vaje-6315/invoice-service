package si.rso.invoice.persistence;

import javax.persistence.*;

@Entity
@Table(name = "invoice_config")
@NamedQueries({
    @NamedQuery(name = InvoiceConfigEntity.FIND_BY_CODE, query = "SELECT c FROM InvoiceConfigEntity c WHERE c.code = :code")
})
public class InvoiceConfigEntity extends BaseEntity {
    
    public static final String FIND_BY_CODE = "InvoiceConfig.findByCode";
    
    @Column(unique = true)
    private String code;
    
    @Column(name = "config_value")
    private String value;
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
