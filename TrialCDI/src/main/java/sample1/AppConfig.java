package sample1;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;


@ApplicationPath("/sample1")
public class AppConfig extends ResourceConfig {
  public AppConfig() {

//    packages(false, this.getClass().getPackage().getName() + ".servlet");
    packages(false, "sample1.servlet");
//    register(IndexResource.class);
//    register(CustomerService.class);
  }
}

//public class AppConfig extends Application {
//    @Override
//    public Set<Class<?>> getClasses() {
//        final Set<Class<?>> classes = new HashSet<>();
//        classes.add(IndexResource.class);
//        classes.add(CustomerService.class);
//
//        return classes;
//    }
//}