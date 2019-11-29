package si.rso.invoice.api.endpoints;

import grpc.Invoice;
import grpc.InvoiceServiceGrpc;
import io.grpc.stub.StreamObserver;
import si.rso.invoice.services.InvoiceService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class GrpcInvoiceEndpoint extends InvoiceServiceGrpc.InvoiceServiceImplBase {
    
    @Inject
    private InvoiceService invoiceService;
    
    @Override
    public void createInvoice(Invoice.InvoiceRequest request, StreamObserver<Invoice.InvoiceResponse> responseObserver) {
        this.invoiceService.createInvoice(request.getOrderId());
        responseObserver.onNext(Invoice.InvoiceResponse.newBuilder()
            .setStatus(200)
            .setInvoiceId("invoiceId")
            .build());
        responseObserver.onCompleted();
    }
}
