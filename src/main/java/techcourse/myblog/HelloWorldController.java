package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {
    @ResponseBody
    @GetMapping("/helloworld")
    public String helloworldGet(@RequestParam String blogName) {
        return blogName;
    }

    @GetMapping("/helloworld2")
    public String getBlogNameByGet2(String blogName, Model model) {
        model.addAttribute("blogName", blogName);
        return "index";
    }

    @ResponseBody
    @PostMapping("/helloworld")
    public String getBlogNameByPost(@RequestBody String blogName) {
        return blogName;
    }
}
