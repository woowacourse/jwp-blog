package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HelloWorldController {

    @ResponseBody
    @GetMapping("/helloworld")
    private String getName(@RequestParam("blogName") String blogName, Model model) {
        model.addAttribute("blogName", blogName);
        return blogName;
    }

    @ResponseBody
    @PostMapping("/helloworld")
    private String getNameByPost(@RequestBody String body, Model model) {
        return body;
    }

}
