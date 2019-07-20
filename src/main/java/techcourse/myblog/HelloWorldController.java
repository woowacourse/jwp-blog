package techcourse.myblog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    @GetMapping("/helloworld")
    public String temp(String blogName) {
        return blogName;
    }

    @PostMapping("/helloworld")
    public String asd(@RequestBody String blogName) {
        return blogName;
    }
}
