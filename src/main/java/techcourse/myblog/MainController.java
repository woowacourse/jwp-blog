package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import techcourse.myblog.domain.ArticleDTO;

@Controller
public class MainController {
    @GetMapping("/writing")
    public String writing(@ModelAttribute ArticleDTO articleDTO) {
        return "article-edit";
    }
}
