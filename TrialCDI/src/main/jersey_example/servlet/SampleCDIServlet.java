package servlet;

import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

@ManagedBean
@ApplicationScoped
@WebServlet(urlPatterns = "/cdisample")
public class SampleCDIServlet extends HttpServlet{

    @Context
    SampleCDIService scopedService;

    @Override
    protected void doGet(HttpServletRequest pReq, HttpServletResponse pResp) throws ServletException, IOException {
        this.scopedService.countUp();
        this.scopedService.countUp();
        pResp.setContentType("text/plain");
        pResp.getWriter().println("Counter Value -> " + this.scopedService.getCounter());
    }
}
