package si.rso.invoice.services;

public interface EventStreamingService {
    
    void handleMessage(String rawMessage);
    
}
