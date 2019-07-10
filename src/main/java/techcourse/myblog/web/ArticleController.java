package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public ModelAndView nameChange(String blogName, ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        modelAndView.addObject("blogName", blogName);
        return modelAndView;
    }

    @GetMapping("/writing")
    public String articleEdit() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle() {
        System.out.println("articles 요청 받음");
        articleRepository.
        return "article";
    }
}
