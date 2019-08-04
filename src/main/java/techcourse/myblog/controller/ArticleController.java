package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;
import java.util.List;

import static techcourse.myblog.controller.ArticleController.ARTICLE_URL;


@Slf4j
@Controller
@RequestMapping(ARTICLE_URL)
public class ArticleController {
    public static final String ARTICLE_URL = "/articles";
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping()
    public String saveArticlePage(HttpSession httpSession, ArticleDto articleDto) {
        User user = (User) httpSession.getAttribute("user");
        long articleId = articleService.save(articleDto, user.getId());
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");

        if (articleService.isNotAuthor(articleId, user.getId())) {
            return "redirect:/";
        }

        model.addAttribute("article", articleService.getArticleOrElseThrow(articleId));
        return "article-edit";
    }

    @GetMapping("{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        List<Comment> comments = articleService.getComments(articleId);
        model.addAttribute("article", articleService.getArticleOrElseThrow(articleId));
        model.addAttribute("comments", comments);
        return "article";
    }

    @PutMapping("{articleId}")
    public String updateArticleByIdPage(HttpSession httpSession, ArticleDto articleDto) {
        User user = (User) httpSession.getAttribute("user");

        if (articleService.isNotAuthor(articleDto.getId(), user.getId())) {
            return "redirect:/";
        }

        Article article = articleService.update(articleDto, user.getId());
        return "redirect:/articles/" + article.getId();
    }

    @DeleteMapping("{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId, HttpSession httpSession) {
        Article article = articleService.getArticleOrElseThrow(articleId);
        User user = (User) httpSession.getAttribute("user");

        if (articleService.isNotAuthor(article.getId(), user.getId())) {
            return "redirect:" + ARTICLE_URL + "/" + articleId;
        }

        articleService.delete(articleId);
        return "redirect:/";
    }

}
