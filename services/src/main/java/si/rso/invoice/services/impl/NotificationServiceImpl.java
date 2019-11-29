package si.rso.invoice.services.impl;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import si.rso.event.streaming.EventStreamMessage;
import si.rso.event.streaming.EventStreamMessageBuilder;
import si.rso.event.streaming.EventStreamMessageParser;
import si.rso.event.streaming.JacksonMapper;
import si.rso.invoice.persistence.InvoiceEntity;
import si.rso.invoice.services.NotificationService;
import si.rso.notifications.lib.ChannelNotification;
import si.rso.notifications.lib.EmailNotification;
import si.rso.notifications.lib.NotificationsStreamConfig;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {
    
    @Inject
    @StreamProducer
    private Producer<String, String> producer;
    
    @Override
    public void sendNotification(InvoiceEntity invoice) {
    
        EmailNotification email = new EmailNotification();
        email.setSubject("Invoice " + invoice.getId());
        email.setEmail("");
        email.setHtmlContent("<h1>Order</h1><p>Invoice created!</p>");
    
        ChannelNotification channelNotification = new ChannelNotification();
        channelNotification.setEmail(email);
        
        EventStreamMessage message = EventStreamMessageBuilder.getInstance()
            .ofType(NotificationsStreamConfig.KAFKA_SEND_NOTIFICATION_EVENT_ID)
            .withData(JacksonMapper.stringify(channelNotification))
            .build();
        
        Optional<String> eventStreamMessage = EventStreamMessageParser.encodeMessage(message);
        
        if (eventStreamMessage.isPresent()) {
    
            ProducerRecord<String, String> record = new ProducerRecord<>(
                NotificationsStreamConfig.KAFKA_NOTIFICATIONS_CHANNEL,
                "key",
                eventStreamMessage.get()
            );
        
            producer.send(record, (meta, exc) -> {
                if (exc != null) {
                    exc.printStackTrace();
                }
            });
        }
    }
}
