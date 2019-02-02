package proto.greeting.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
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
	 * URLのマッピングをapplication.ymlに設定されているに変更する
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




	//NG:
	// クラスパス直下を読みに行くがサーブレットが動作しなくなるのでNG
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**")
//				.addResourceLocations("/")
//				.setCachePeriod(0);
//	}
}
