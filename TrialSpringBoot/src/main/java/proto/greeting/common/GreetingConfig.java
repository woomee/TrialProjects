package proto.greeting.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
//@EnableWebMvc
public class GreetingConfig implements WebMvcConfigurer  {

	/**
	 * Greeting向けのアプリケーション名を取得する
	 */
	@Value("${greeting.app}")
	private String app;
	public String getApp() {
		return app;
	}

	/**
	 * URLのマッピングをapplication.ymlのappに設定されているフォルダに変更する
	 *
	 * Filterを使ってURLから適切なリクエストにforwardする。
	 * ただ、SpringのControllerで作成したサーブレットマッピング(@RequestMapping)は
	 * RequestMappingHandlerMappingから取得するので別Beanを利用する。
	 *
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<GreetingFilter> greetingFilter() {
		GreetingFilter greetingFilter = new GreetingFilter(this.app);
		FilterRegistrationBean<GreetingFilter> bean =
				new FilterRegistrationBean<GreetingFilter>(greetingFilter);
		bean.addUrlPatterns("/*");
		bean.setOrder(Integer.MIN_VALUE);
		return bean;
	}

	/**
	 * URLのマッピングをapplication.ymlのappに設定されているサーブレットに変更する
	 *
	 * @return
	 */
	// 以下の実装のように@EnableWebMvcを付けるとwebappやwebjarsが利用できなくなる
// https://stackoverflow.com/questions/30402453/how-to-add-requestmappinghandlermapping-and-resourcehandlers-to-a-springmvc-conf?rq=1
//	@Bean
//	public RequestMappingHandlerMapping requestMappingHandlerMapping() {
//		return new RasinvanRequestMappingHandlerMapping();
//	}
    @Bean
    public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new GreetingRequestMappingHandlerMapping();
            }
        };
    }
}
