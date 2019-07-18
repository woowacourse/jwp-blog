package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("/err")
    public String err(String errMessage, Model model) {
        model.addAttribute("errMessage", errMessage);
        return "error-page";
    }
}
