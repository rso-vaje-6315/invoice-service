package si.rso.invoice.services.impl;

import si.rso.event.streaming.EventStreamMessage;
import si.rso.event.streaming.EventStreamMessageParser;
import si.rso.event.streaming.JacksonMapper;
import si.rso.invoice.services.EventStreamingService;
import si.rso.invoice.services.InvoiceService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class EventStreamingServiceImpl implements EventStreamingService {

    @Inject
    private InvoiceService invoiceService;
    
    @Override
    public void handleMessage(String rawMessage) {
        Optional<EventStreamMessage> eventStreamMessage = EventStreamMessageParser.decodeMessage(rawMessage);
        
        if (eventStreamMessage.isPresent()) {
            
            if (eventStreamMessage.get().getType().equals("ORDER_FINISHED")) {
                
                // TODO: insert type
                Object orderDetails = JacksonMapper.toEntity(eventStreamMessage.get().getData(), Object.class);
                invoiceService.createInvoice("orderId");
            } else {
                // type not handled by this service
            }
        } else {
            // badly formed message
        }
    }
}
