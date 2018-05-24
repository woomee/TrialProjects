package sample1.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("simple")
public class SimpleServlet {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getText() {
		return "simple";
	}
}