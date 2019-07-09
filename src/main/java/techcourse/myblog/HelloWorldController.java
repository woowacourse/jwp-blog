package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

    @GetMapping("/helloworld")
    @ResponseBody
    public String helloWorldGet(String blogName) {
        return blogName;
    }

    @PostMapping("/helloworld")
    @ResponseBody
    public String helloWorldPost(@RequestBody String blogName) {
        return blogName;
    }

}
