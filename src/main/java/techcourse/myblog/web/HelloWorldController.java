package techcourse.myblog.web;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {
    @GetMapping
    public String get(String blogName) {
        return blogName;
    }

    @PostMapping
    public String post(@RequestBody String blogName) {
        return blogName;
    }

}
