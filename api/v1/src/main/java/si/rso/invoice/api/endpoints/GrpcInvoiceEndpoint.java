package si.rso.invoice.api.endpoints;

import com.kumuluz.ee.grpc.annotations.GrpcService;
import grpc.Invoice;
import grpc.InvoiceServiceGrpc;
import io.grpc.stub.StreamObserver;
import si.rso.invoice.services.InvoiceService;

import javax.enterprise.inject.spi.CDI;

@GrpcService
public class GrpcInvoiceEndpoint extends InvoiceServiceGrpc.InvoiceServiceImplBase {
    
    @Override
    public void createInvoice(Invoice.InvoiceRequest request, StreamObserver<Invoice.InvoiceResponse> responseObserver) {
        InvoiceService invoiceService = CDI.current().select(InvoiceService.class).get();
        var invoice = invoiceService.createInvoice(request.getOrderId());
        
        responseObserver.onNext(Invoice.InvoiceResponse.newBuilder()
            .setStatus(200)
            .setInvoiceId(invoice.getId())
            .build());
        
        responseObserver.onCompleted();
    }
}
