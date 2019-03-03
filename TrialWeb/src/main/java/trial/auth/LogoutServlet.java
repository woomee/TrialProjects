package trial.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ログアウト処理としてセッションを破棄する
 *
 * @author umino
 *
 */
@WebServlet(name="LogoutServlet", urlPatterns = "/logout")
public class LogoutServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession(false);
		if (session != null) {
			System.out.println("Logout: session invalidate");
			session.invalidate();
		}
		else {
			System.out.println("Logout: session is null");
		}

		/* 認証エリア（ログインページ)へ戻す */
		String transUrl = req.getContextPath() +   "/basic/";
		int status = 401;
		System.out.println("Tranfer URL=" + transUrl +  ", Set status " + status);
//		resp.setStatus(status);
		resp.sendRedirect(req.getContextPath() +   "/basic/");
		//req.getRequestDispatcher("/basic").forward(req, resp);

	}

}
