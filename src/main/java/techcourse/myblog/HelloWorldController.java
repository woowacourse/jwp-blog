package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @GetMapping("/")
    public String passParamWithGet() {
        return "index";
    }

//    @PostMapping
//    public String passParamWithPost(@RequestBody String blogName){
//        return blogName;
//    }
}
