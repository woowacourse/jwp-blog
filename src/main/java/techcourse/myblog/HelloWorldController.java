package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @ResponseBody
    @GetMapping("/helloworld")
    public String getBlogNameUsingGet(String blogName) {
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String getBlogNameUsingPost(@RequestBody String blogName) {
        return blogName;
    }

    @ResponseBody
    @GetMapping("/{blogName}")
    public String changeBlogName(@PathVariable("blogName") String name) { //@PathVariable
        return name;
    }
}