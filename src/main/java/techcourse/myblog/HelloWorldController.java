package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {
    @ResponseBody
    @GetMapping("/helloworld")
    public String getHelloWorld(@RequestParam("blogName") final String blogName) {
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String postHelloWorld(@RequestBody String blogName) {
        return blogName;
    }
}
