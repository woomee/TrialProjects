package proto.greeting.common;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class GreetingFilter extends OncePerRequestFilter {

	private String appName;
	public GreetingFilter(String appName) {
		this.appName = appName;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		/*  /commonを/{appName}でリプレースしてパスを検索 */
		String requestPath = request.getRequestURI();
		String appPath = requestPath.replace("/common", "/" + this.appName);

		// NG: Springのサーブレットマッピングは取得できない
//		Map<String, ? extends ServletRegistration> servletMap = request.getServletContext().getServletRegistrations();
//		for (Iterator<?> iterator = servletMap.entrySet().iterator(); iterator.hasNext();) {
//			Map.Entry<String, ? extends ServletRegistration> entry = (Entry<String, ? extends ServletRegistration>) iterator.next();
//			String servletName = entry.getKey();
//			ServletRegistration servletRegist = entry.getValue();
//
//			for (Iterator<String> iterator2 = servletRegist.getMappings().iterator(); iterator2.hasNext();) {
//				String  mapping = iterator2.next();
//				System.out.println(servletName + ":" + mapping);	// "dispatcherServlet:/"しか取得できない
//			}
//		}

		/* ファイルが存在する場合はフォワードする */
		if (hasFileMapping(appPath, request)) {
			RequestDispatcher rd = request.getRequestDispatcher(appPath);
			rd.forward(request, response);
			return;
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * 指定されたパスのファイルが存在すればtrue
	 *
	 * @param appPath
	 * @param request
	 * @return
	 */
	private boolean hasFileMapping(String appPath, HttpServletRequest request) {
		String appRealPath = request.getServletContext().getRealPath(appPath);
		File appFile = new File(appRealPath);
		return appFile.exists();
	}
}
