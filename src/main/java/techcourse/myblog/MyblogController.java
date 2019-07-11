package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyblogController {
    @GetMapping("/writing")
    public String getWriting() {
        return "article-edit";
    }
}
