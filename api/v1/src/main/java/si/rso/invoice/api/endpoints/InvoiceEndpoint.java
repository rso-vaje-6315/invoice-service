package si.rso.invoice.api.endpoints;

import com.kumuluz.ee.rest.beans.QueryParameters;
import si.rso.invoice.lib.Invoice;
import si.rso.invoice.services.InvoiceService;
import si.rso.rest.common.HttpHeaders;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/test")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceEndpoint {
    
    @Context
    protected UriInfo uriInfo;
    
    @Inject
    private InvoiceService invoiceService;
    
    @GET
    public Response getInvoices() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Invoice> invoices = invoiceService.getInvoices(query);
        long invoicesCount = invoiceService.getInvoicesCount(query);
        return Response.ok(invoices).header(HttpHeaders.X_TOTAL_COUNT, invoicesCount).build();
    }
    
    @GET
    @Path("/{id}")
    public Response getInvoice(@PathParam("id") String orderId) {
        Invoice invoice = invoiceService.getInvoice(orderId);
        return Response.ok(invoice).build();
    }
}
