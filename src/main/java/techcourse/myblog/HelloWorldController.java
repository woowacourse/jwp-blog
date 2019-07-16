package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @ResponseBody
    @GetMapping("/helloworld")
    public String helloWord(@RequestParam String blogName) {
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String helloWord2(@RequestBody String blogName) {
        return blogName;
    }
}
