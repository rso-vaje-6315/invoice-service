package si.rso.invoice.services;

import com.kumuluz.ee.rest.beans.QueryParameters;
import si.rso.invoice.lib.Invoice;

import java.util.List;

public interface InvoiceService {
    
    /**
     * Creates invoice for given order. Generates printable invoice, stores it into S3 and sends it to customer.
     * @param orderId Order to generate invoice for
     */
    void createInvoice(String orderId);
    
    List<Invoice> getInvoices(QueryParameters query);
    
    long getInvoicesCount(QueryParameters query);
    
    Invoice getInvoice(String invoiceId);
    
    
}
