package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public ModelAndView getIndex(String blogName, ModelAndView modelAndView) {
        if (blogName == null) {
            blogName = "누구게?";
        }
        modelAndView.setViewName("index");
        modelAndView.addObject("blogName", blogName);
        modelAndView.addObject("articleRepository", articleRepository);
        return modelAndView;
    }

    @GetMapping("/writing")
    public String editArticle() {
        return "article-edit";
    }

    //TODO 여기는 annotation은 어딧지? @ResponseBody는 언제 붙이고 이때는 왜 생략해야될까?
    @PostMapping("/articles")
    public ModelAndView writeArticle(Article article, ModelAndView modelAndView) {
        articleRepository.add(article);
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/article/{articleId}")
    public ModelAndView showArticle(@PathVariable String articleId, ModelAndView modelAndView) {
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView editArticle(@PathVariable String articleId, ModelAndView modelAndView) {
        modelAndView.setViewName("article-edit");
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable String articleId, Article article) {
        articleRepository.updateArticle(Integer.parseInt(articleId), article);
        return "redirect:/";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable String articleId) {
        articleRepository.deleteById(Integer.parseInt(articleId));
        return "redirect:/";
    }
}
