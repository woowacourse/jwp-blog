package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

//TODO 메서드명에 대해서 알아보기 메서드 명에 Article과 같은게 들어가는게 더 나은지
//아니면 이 상태로 괜찮은지?
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
    public String edit() {
        return "article-edit";
    }

    //TODO 여기는 annotation은 어딧지? @ResponseBody는 언제 붙이고 이때는 왜 생략해야될까?
    @PostMapping("/articles")
    public ModelAndView save(Article article, ModelAndView modelAndView) {
        articleRepository.save(article);
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/article/{articleId}")
    public ModelAndView show(@PathVariable String articleId, ModelAndView modelAndView) {
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView edit(@PathVariable String articleId, ModelAndView modelAndView) {
        modelAndView.setViewName("article-edit");
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable String articleId, Article article) {
        articleRepository.updateArticle(Integer.parseInt(articleId), article);
        return "redirect:/";
    }

    @DeleteMapping("/articles/{articleId}")
    public String delete(@PathVariable String articleId) {
        articleRepository.deleteById(Integer.parseInt(articleId));
        return "redirect:/";
    }
}
