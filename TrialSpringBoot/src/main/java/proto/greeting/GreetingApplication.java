package proto.greeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class GreetingApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(GreetingApplication.class, args);

		//SpringApplication app = new SpringApplication(Application.class);
		//app.setWebApplicationType(WebApplicationType.NONE);
		//app.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GreetingApplication.class);
	}

}