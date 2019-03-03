package trial.auth;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.mail.internet.MimeUtility;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * http://typea.info/tips/wiki.cgi?page=Filter%A4%C7Basic%C7%A7%BE%DA%A4%F2%BC%C2%C1%F5
 *
 * @author umino
 *
 */

//@WebFilter(filterName = "sample-filter", urlPatterns = "/*")
public class BasicAuthenticationFilter implements Filter {
	/**
	 * レルム名
	 */
	private final String realmName = "UserDatabaseRealm";

	/* @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain) */
	public void doFilter(ServletRequest request,
			ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		System.out.println("In Filter");


		ByteArrayInputStream bin = null;
		BufferedReader br = null;
		try {
			HttpServletRequest httpReq = (HttpServletRequest) request;

			String basicAuthData = httpReq.getHeader("authorization");
			if (basicAuthData == null) {
				System.out.println("In Filter");
				filterChain.doFilter(request, response);
				return;
				//sendError((HttpServletResponse)response);
			}

			// Basic認証から情報を取得
			String basicAuthBody = basicAuthData.substring(6); // ex 'Basic dG9tY2F0OnRvbWNhdA== '
			bin = new ByteArrayInputStream(basicAuthBody.getBytes());
			br = new BufferedReader(
					new InputStreamReader(MimeUtility.decode(bin, "base64")));

			StringBuilder buf = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line);
			}
			String[] loginInfo = buf.toString().split(":");
			String username = loginInfo[0] == null ? "" : loginInfo[0];
			String password = loginInfo[1] == null ? "" : loginInfo[1];
			System.out.println("User=" + username + ", Pass=" + password);

			boolean isAuthorized = isAuthorized(username, password);

			if (!isAuthorized) {
				sendError((HttpServletResponse)response);
			} else {
				filterChain.doFilter(request, response);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try {
				if (bin != null)
					bin.close();
				if (br != null)
					br.close();
			} catch (Exception e) {
			}
		}
	}

	private void sendError(HttpServletResponse response) throws IOException {
		System.out.println("sendError");

		//ブラウザに UnAuthorizedエラー(401)を返す
		HttpServletResponse httpRes = (HttpServletResponse) response;
		httpRes.setHeader("WWW-Authenticate", "Basic realm=" + this.realmName);
		httpRes.setContentType("text/html");
		httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED); // 401

	}

	private boolean isAuthorized(String user, String pass) {
		if ("test".equals(user) && "pass".equals(pass)) {
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void destroy() {
		// TODO 自動生成されたメソッド・スタブ

	}

}
