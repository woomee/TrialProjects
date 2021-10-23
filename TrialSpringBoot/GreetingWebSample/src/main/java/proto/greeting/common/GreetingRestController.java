package proto.greeting.common;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingRestController {

    private static final String template = "Common: Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path="/common/greeting")
    public Greeting greeting(
    			@RequestParam(value="name", defaultValue="World")
    			String name) {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }
}