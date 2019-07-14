package techcourse.myblog;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {
    @GetMapping("/helloworld")
    public String blogNameByGet(String blogName) {
        return blogName;
    }

    @PostMapping("/helloworld")
    public String blogNameByPost(@RequestBody String blogName) {
        return blogName;
    }
}
