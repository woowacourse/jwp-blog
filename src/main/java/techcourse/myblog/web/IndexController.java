package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techcourse.myblog.domain.BlogName;

@Controller
public class IndexController {

    @GetMapping("/")
    public String getIndex(final Model model, @RequestParam(value = "blogName", required = false) final String blogName) {
        final BlogName name = new BlogName(blogName);
        model.addAttribute("blogName", name);
        return "index";
    }
}
