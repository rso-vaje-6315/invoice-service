package si.rso.invoice.api.endpoints;

import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.rest.beans.QueryParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.eclipse.microprofile.metrics.annotation.Counted;
import si.rso.invoice.lib.Invoice;
import si.rso.invoice.services.InvoiceService;
import si.rso.rest.common.HttpHeaders;
import si.rso.rest.exceptions.dto.ExceptionResponse;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Log
@Path("/invoices")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceEndpoint {
    
    @Context
    protected UriInfo uriInfo;
    
    @Inject
    private InvoiceService invoiceService;
    
    @Operation(description = "Returns invoices.",
        summary = "Returns invoice list", tags = "invoice",
        responses = {
            @ApiResponse(responseCode = "200", description = "Returns invoices.",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = Invoice.class))))
        })
    @GET
    public Response getInvoices() {
        QueryParameters query = QueryParameters.query(uriInfo.getRequestUri().getQuery()).build();
        List<Invoice> invoices = invoiceService.getInvoices(query);
        long invoicesCount = invoiceService.getInvoicesCount(query);
        return Response.ok(invoices).header(HttpHeaders.X_TOTAL_COUNT, invoicesCount).build();
    }
    
    @Operation(description = "Returns invoice with given id.",
        summary = "Returns invoice.", tags = "invoice",
        responses = {
            @ApiResponse(responseCode = "200", description = "Returns invoice.",
                content = @Content(schema = @Schema(implementation = Invoice.class))),
            @ApiResponse(responseCode = "404", description = "Invoice not found.",
                content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        })
    @GET
    @Path("/{id}")
    @Counted(name = "get-invoice-count")
    public Response getInvoice(@PathParam("id") String orderId) {
        Invoice invoice = invoiceService.getInvoice(orderId);
        return Response.ok(invoice).build();
    }
    
}
