package techcourse.myblog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
