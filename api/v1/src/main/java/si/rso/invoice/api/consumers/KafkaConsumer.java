package si.rso.invoice.api.consumers;

import com.kumuluz.ee.streaming.common.annotations.StreamListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import si.rso.invoice.services.EventStreamingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KafkaConsumer {
    
    @Inject
    private EventStreamingService eventStreamingService;
    
    @StreamListener(topics = {"ORDERS"})
    public void onMessage(ConsumerRecord<String, String> record) {
        eventStreamingService.handleMessage(record.value());
    }

}
