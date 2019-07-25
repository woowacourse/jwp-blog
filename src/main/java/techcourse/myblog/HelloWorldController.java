package techcourse.myblog;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {

    @GetMapping("/helloworld")
    public String helloworld(String blogName) {
        return blogName;
    }

    @PostMapping("/helloworld")
    public String helloworld2(@RequestBody String blogName) {
        return blogName;
    }
}
