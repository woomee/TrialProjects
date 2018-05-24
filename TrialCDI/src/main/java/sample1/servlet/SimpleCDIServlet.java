package sample1.servlet;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample1.service.CDIService;

@WebServlet(urlPatterns = "/simplecdi")
public class SimpleCDIServlet extends HttpServlet{

	@Inject
	private CDIService service;

    @Override
    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException {
        pResp.setContentType("text/plain");
        pResp.getWriter().println("Counter Value -> " + service.countUp());
    }
}
