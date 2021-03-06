package si.rso.invoice.services.impl;

import com.kumuluz.ee.streaming.common.annotations.StreamProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import si.rso.event.streaming.EventStreamMessage;
import si.rso.event.streaming.EventStreamMessageBuilder;
import si.rso.event.streaming.JacksonMapper;
import si.rso.invoice.persistence.InvoiceEntity;
import si.rso.invoice.services.NotificationService;
import si.rso.notifications.lib.ChannelNotification;
import si.rso.notifications.lib.EmailNotification;
import si.rso.notifications.lib.NotificationsStreamConfig;
import si.rso.notifications.lib.SmsNotification;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class NotificationServiceImpl implements NotificationService {
    
    @Inject
    @StreamProducer
    private Producer<String, EventStreamMessage> producer;
    
    @CircuitBreaker
    @Override
    public void sendNotification(InvoiceEntity invoice, String customerEmail, String customerPhone) {
    
        EmailNotification email = new EmailNotification();
        email.setSubject("Invoice " + invoice.getId());
        email.setEmail(customerEmail);
        email.setHtmlContent("<h1>Order</h1><p>Invoice created!</p>");
    
        var attachment = new EmailNotification.EmailAttachment();
        attachment.setName("invoice_" + invoice.getId() + ".pdf");
        attachment.setUrl(invoice.getInvoiceUrl());
        email.setAttachment(attachment);
    
        SmsNotification smsNotification = new SmsNotification();
        smsNotification.setContent("Order with id '" + invoice.getOrderId() + "' was fulfilled! It will be delivered soon.");
        smsNotification.setPhoneNumber(customerPhone);
    
        ChannelNotification channelNotification = new ChannelNotification();
        channelNotification.setEmail(email);
        channelNotification.setSms(smsNotification);
        
        EventStreamMessage message = EventStreamMessageBuilder.getInstance()
            .ofType(NotificationsStreamConfig.SEND_NOTIFICATION_EVENT_ID)
            .withData(JacksonMapper.stringify(channelNotification))
            .build();
    
        ProducerRecord<String, EventStreamMessage> record = new ProducerRecord<>(
            NotificationsStreamConfig.NOTIFICATIONS_CHANNEL, "key", message
        );
    
        producer.send(record, (meta, exc) -> {
            if (exc != null) {
                exc.printStackTrace();
            }
        });
    }
}
