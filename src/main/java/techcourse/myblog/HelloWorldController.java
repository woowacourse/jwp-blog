package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {
    @GetMapping("/helloworld")
    @ResponseBody
    public String blogNameByGet(String blogName) {
        return blogName;
    }

    @PostMapping("/helloworld")
    @ResponseBody
    public String blogNameByPost(@RequestBody String blogName) {
        return blogName;
    }
}
