package proto.greeting.common;

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
			HttpServletRequest customReq = new CustomHttpServletRequest(request, appPath);
			return super.lookupHandlerMethod(appPath, customReq);
		}

		return super.lookupHandlerMethod(lookupPath, request);
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
		// Streamを使った実装
		return handlerMap.keySet().stream()
					.anyMatch(v -> v.getPatternsCondition().getPatterns().stream()
					.anyMatch(v2 -> v2.equals(appPath)));




	}


	/**
	 * PathのURLを変更するためのHttpServletRequest
	 *
	 * getServletPath()を指定されたURLに変更している
	 *
	 */
	private static class CustomHttpServletRequest extends HttpServletRequestWrapper {
		private String appPath;
		public CustomHttpServletRequest(HttpServletRequest request, String appPath) {
			super(request);
			this.appPath = appPath;
		}

		@Override
		public String getServletPath() {
			return this.appPath;
		}
	}
}
