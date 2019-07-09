package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @ResponseBody
    @GetMapping("/helloworld")
    public String helloWorld(String blogName) {
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String helloWord(@RequestBody String blogName) {
        return blogName;
    }


    @GetMapping("/hello")
    public String helloWorld2(String blogName, Model model) {
        model.addAttribute("blogName", blogName);
        return "index";
    }

}
