package kajitool.web.config;

// import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// import static com.google.common.base.Predicates.containsPattern;
// import static com.google.common.base.Predicates.or;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
    // for swagger 2.9
    // @Bean
    // public Docket document() {
    //     return new Docket(DocumentationType.SWAGGER_2)
    //             .select().paths(paths()).build();
    // }
    // private Predicate<String> paths() {
    //     return or(containsPattern("/api*"));
    // }

    // for swagger 3.0
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.regex("/api/.*"))                          
          .build();                                           
    }
}