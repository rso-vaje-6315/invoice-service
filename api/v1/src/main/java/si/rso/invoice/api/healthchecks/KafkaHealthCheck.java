package si.rso.invoice.api.healthchecks;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.net.Socket;

@Liveness
@ApplicationScoped
public class KafkaHealthCheck implements HealthCheck {
    
    @Override
    public HealthCheckResponse call() {
        if (checkKafkaEndpoint()) {
            return HealthCheckResponse.named(KafkaHealthCheck.class.getSimpleName()).up().build();
        }
        return HealthCheckResponse.named(KafkaHealthCheck.class.getSimpleName()).down().build();
    }
    
    private boolean checkKafkaEndpoint() {
        String[] kafkaUrl = ConfigurationUtil.getInstance().get("kumuluzee.streaming.kafka.producer.bootstrap-servers").get().split(":");
        String kafkaHost = kafkaUrl[0];
        int kafkaPort = Integer.parseInt(kafkaUrl[1]);
        try (Socket socket = new Socket(kafkaHost, kafkaPort)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
