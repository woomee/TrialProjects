package proto.greeting.custom;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingControllerCustom {

	private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(path="/custom/greeting")		// pathは/{app_name}/{対象操作}というルールにする
    public GreetingCustom greeting(
    			@RequestParam(value="name", defaultValue="World")
    			String name) {

    	/* 日付を追加する */
    	String dateTime = (new Date()).toString();
        return new GreetingCustom(counter.incrementAndGet(),
                            String.format(template, name), dateTime);
    }
}