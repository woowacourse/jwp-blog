package techcourse.myblog.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/helloworld")
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
