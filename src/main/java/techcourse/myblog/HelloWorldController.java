package techcourse.myblog;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {

    @ResponseBody
    @GetMapping("/helloworld")
    public String helloworld(String blogName) {
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String helloworldbypost(@RequestBody String blogName) {
        return blogName;
    }
}
