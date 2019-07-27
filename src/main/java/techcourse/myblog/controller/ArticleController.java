package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.ArticleService;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/writing")
    public String articleForm(){
        return "article-edit";
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto){
        articleService.save(articleDto);
        return "redirect:/articles";
    }




}
