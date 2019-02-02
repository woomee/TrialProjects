package proto.greeting.common;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class GreetingRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	@Autowired
	private GreetingConfig greetingConfig;

	@Override
	protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {

		/*  /commonを/{appName}でリプレースしてパスを検索 */
		String appName = greetingConfig.getApp();
		String appPath = lookupPath.replace("/common", "/" + appName);


		if (hasServletMappping(appPath, request)) {
			HttpServletRequest customReq = new CustomHttpServletRequest(request, appName);
			return super.lookupHandlerMethod(appPath, customReq);
		}

		if (hasFileMapping(appPath, request)) {
			HttpServletRequest forwardReq = new ForwardHttpServletRequest(request, appPath);
			return super.lookupHandlerMethod(forwardReq.getServletPath(), forwardReq);
		}


		return super.lookupHandlerMethod(lookupPath, request);
	}

	private boolean hasMappping(String appPath, HttpServletRequest request) {
		return hasServletMappping(appPath, request) || hasFileMapping(appPath, request);

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

	/**
	 * 指定されたパスを持つサーブレットがあればtrue
	 *
	 * @param appPath
	 * @return
	 */
	private boolean hasServletMappping(String appPath, HttpServletRequest request) {

		// NG: 動作しない
		//return 	getPathMatcher().isPattern(appPath);

		// NG: 動作しない。maps==null
//		MappedInterceptor[] maps = getMappedInterceptors();
//		for (int i = 0; i < maps.length; i++) {
//			if (maps[i].getPathMatcher().isPattern(appPath)) {
//				return true;
//			}
//		}
//		return false;

		// OK:
		Map<RequestMappingInfo, HandlerMethod> handlerMap = getHandlerMethods();
//		for (Iterator<RequestMappingInfo> iterator = handlerMap.keySet().iterator(); iterator.hasNext();) {
//			RequestMappingInfo info = iterator.next();
//			if (info.getPatternsCondition().getPatterns().contains(appPath)) {
//				return true;
//			}
//		}
//		return false;
		return handlerMap.keySet().stream()
					.anyMatch(v -> v.getPatternsCondition().getPatterns().stream()
					.anyMatch(v2 -> v2.equals(appPath)));




	}


	/**
	 * PathのURLを変更するためのHttpServletRequest
	 *
	 *
	 */
	private static class CustomHttpServletRequest extends HttpServletRequestWrapper {
		private String appName;
		public CustomHttpServletRequest(HttpServletRequest request, String appName) {
			super(request);
			this.appName = appName;
		}

		@Override
		public String getServletPath() {
			return replaceCommonPath(super.getServletPath());
		}

		@Override
		public String getRequestURI() {
			return replaceCommonPath(super.getRequestURI());
		}

		@Override
		public StringBuffer getRequestURL() {
			String appUrl = replaceCommonPath(super.getRequestURL().toString());
			return new StringBuffer(appUrl);
		}

		private String replaceCommonPath(String commonPath) {
			return commonPath.replace("/common", "/" + this.appName);
		}
	}


	private static class ForwardHttpServletRequest extends HttpServletRequestWrapper {
		private String forwardPath;
		public ForwardHttpServletRequest(HttpServletRequest request, String forwardPath) {
			super(request);
			this.forwardPath = forwardPath;
		}

		@Override
		public String getServletPath() {
			return "/common/forward?url=" + forwardPath;
//			return "/common/forward";
		}

//		@Override
//		public String getQueryString() {
//			return "url=" + forwardPath;
//		}


	}

}
