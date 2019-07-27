package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    @GetMapping("/err")
    public String err(Model model) {
        String errMessage = (String) model.asMap().get("message");
        model.addAttribute("message", errMessage);

        return "error-page";
    }
}
