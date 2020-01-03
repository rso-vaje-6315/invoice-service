package si.rso.invoice.api.healthchecks;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Liveness
@ApplicationScoped
public class ConsulHealthCheck implements HealthCheck {
    
    @Override
    public HealthCheckResponse call() {
        String agentUrl = ConfigurationUtil.getInstance().get("kumuluzee.config.consul.agent").orElse("http://localhost:8500");
        String healthUrl = agentUrl + "/v1/status/leader";
    
        Response response = ClientBuilder.newClient()
            .target(healthUrl)
            .request(MediaType.APPLICATION_JSON)
            .build(HttpMethod.GET)
            .invoke();
        
        if (response.getStatus() == 200) {
            return HealthCheckResponse.named(ConsulHealthCheck.class.getSimpleName())
                .up()
                .withData(healthUrl, "UP")
                .build();
        }
        return HealthCheckResponse.named(ConsulHealthCheck.class.getSimpleName()).down().build();
    }
}
