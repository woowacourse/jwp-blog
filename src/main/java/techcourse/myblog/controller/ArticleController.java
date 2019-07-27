package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
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
        Long newArticleId = articleService.save(articleDto);
        log.error(newArticleId+"asdf");
        return "redirect:/articles/" + newArticleId;
    }

}

