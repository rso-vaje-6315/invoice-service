package si.rso.invoice.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("service-config")
public class ServiceConfig {
    
    @ConfigValue(value = "maintenance", watch = true)
    private boolean maintenance;
    
    public boolean isMaintenance() {
        return maintenance;
    }
    
    public void setMaintenance(boolean maintenance) {
        this.maintenance = maintenance;
    }
}
