package servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/cdisample2")
public class SampleCDIServlet2 {

    @Context
    SampleCDIService scopedService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String doGet() {
        this.scopedService.countUp();
        this.scopedService.countUp();
        return "Counter Value -> " + this.scopedService.getCounter();
    }
}
