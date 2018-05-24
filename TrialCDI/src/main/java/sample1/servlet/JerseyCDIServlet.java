package sample1.servlet;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import sample1.service.CDIService;

@Path("custom")
public class JerseyCDIServlet {

	@Inject
	CDIService service;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getText() {
		return "Count = " + service.countUp();
	}
}