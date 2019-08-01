package techcourse.myblog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.service.ArticleService;
import techcourse.myblog.domain.service.CommentService;
import techcourse.myblog.dto.ArticleDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final String USER = "user";
    private static final String ARTICLE = "article";

    private final ArticleService articleService;
    private final CommentService commentService;

    @Autowired
    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @PostMapping("")
    public String createArticle(@Valid ArticleDto newArticleDto, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute(USER);
        Article article = articleService.save(newArticleDto.toEntity(author));
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}")
    public String selectArticle(@PathVariable long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute(ARTICLE, article);
        model.addAttribute("comments", commentService.findByArticle(article));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String moveArticleEditPage(@PathVariable long articleId, Model model, HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute(USER);
        Article article = articleService.findById(articleId, loginUser.getId());
        model.addAttribute(ARTICLE, article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable long articleId, @Valid ArticleDto updateArticleDto, Model model, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute(USER);
        Article updateArticle = articleService.update(articleId, updateArticleDto.toEntity(author));
        model.addAttribute(ARTICLE, updateArticle);
        return "redirect:/articles/" + updateArticle.getId();
    }

    //TODO : comment delete 할때 CASECADE 하면 조금 코멘트도 같이 삭제 된다.
    //TODO: articleId를 delete? article.getId()?
    //TODO: 중복제거..?

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable long articleId, HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute(USER);
        Article article = articleService.findById(articleId, loginUser.getId());
        articleService.deleteById(article.getId());
        return "redirect:/";
    }
}
