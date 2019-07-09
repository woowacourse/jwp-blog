package techcourse.myblog;

import org.springframework.web.bind.annotation.*;

@RestController("/" +
        "")
public class HelloWorldController {

    @GetMapping
    public String passParamWithGet(String blogName) {
        return blogName;
    }

    @PostMapping
    public String passParamWithPost(@RequestBody String blogName){
        return blogName;
    }
}
