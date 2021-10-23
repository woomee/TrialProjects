package proto.greeting.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @RequestMapping(path="/common/forward")
    public String forward(@RequestParam(name="url") String url) {
    	return "forward:" + url;
    }
}