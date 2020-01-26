package owb.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import owb.service.CDIService;

@WebServlet(urlPatterns = "/simplecdi")
public class SimpleCDIServlet extends HttpServlet {

	@Inject
	private CDIService service;

	@Override
	protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException {
		try {
			//pResp.getWriter().println("Counter Value -> " + service.countUp());
			pResp.setContentType("text/plain");
			int count = service.countUp();
//			int count = 10;
			pResp.getWriter().print(count);
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
