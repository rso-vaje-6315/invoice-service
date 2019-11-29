package si.rso.invoice.apis;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import si.rso.customers.lib.CustomerDetails;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@RegisterRestClient
@Dependent
public interface CustomersApi {

    @GET
    @Path("")
    CustomerDetails getDetails();

}
