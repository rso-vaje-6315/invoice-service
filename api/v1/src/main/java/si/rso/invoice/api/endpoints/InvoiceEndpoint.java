package si.rso.invoice.api.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kumuluz.ee.logs.cdi.Log;
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
    
    @GET
    @Path("/test")
    public Response test() {
        this.invoiceService.createInvoice("tra");
        return Response.ok().build();
    }
    
    @GET
    @Path("/config")
    public Response getConfig() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseNode = mapper.createObjectNode();
        
        ObjectNode vatNode = mapper.createObjectNode();
        vatNode.put("vatRate", "0.22");
        responseNode.set("vat", vatNode);
    
        ObjectNode companyNode = mapper.createObjectNode();
        
        companyNode.put("name", "Sellers d.o.o.");
        companyNode.put("address", "Ljubljanska ulica 1, 1000 Ljubljana");
        companyNode.put("phone", "+386134768812");
        companyNode.put("email", "sellers@mail.com");
        responseNode.set("company", companyNode);
        
        return Response.ok(responseNode).build();
    }
}
