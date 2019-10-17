package si.rso.invoice.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

// @RegisterRestClient(configKey = "order-service")
// @Dependent
// @Path("/orders")
public interface OrderService {
    
    @GET
    @Path("/{orderId}")
    Object getOrder(@PathParam("orderId") String orderId);
    
}
