package si.rso.invoice.services;

import si.rso.invoice.persistence.InvoiceEntity;

public interface NotificationService {
    
    void sendNotification(InvoiceEntity invoice, String customerEmail);
    
}
