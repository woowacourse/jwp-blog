package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @GetMapping("/helloworld")
    @ResponseBody
    public String passParamWithGet(String blogName) {
        return blogName;
    }

    @PostMapping("/helloworld")
    @ResponseBody
    public String passParamWithPost(@RequestBody String blogName){
        return blogName;
    }
}
