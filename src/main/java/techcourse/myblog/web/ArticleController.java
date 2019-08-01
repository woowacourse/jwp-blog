package techcourse.myblog.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;
import java.util.List;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(final ArticleService articleService, final CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(ArticleSaveRequestDto articleSaveRequestDto, HttpSession httpSession) {
        log.info("save article post request params={}", articleSaveRequestDto);
        Article article = articleService.save(articleSaveRequestDto, (User) httpSession.getAttribute(USER));
        Long id = article.getId();
        return "redirect:/articles/" + id;
    }

    @GetMapping("/{id}")
    public String fetchArticle(@PathVariable long id, Model model) {
        Article article = articleService.findById(id);
        List<Comment> comments = commentService.findByArticleId(id);

        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editArticle(@PathVariable long id, Model model, HttpSession httpSession) {
        Article article = articleService.findById(id);

        if (!isUserEqualsAuthor(id, httpSession)) {
            return "redirect:/articles/" + id;
        }
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String saveEditedArticle(@PathVariable long id, ArticleSaveRequestDto articleSaveRequestDto, HttpSession httpSession) {
        log.info("save edited article post request params={}", articleSaveRequestDto);

        if (isUserEqualsAuthor(id, httpSession)) {
            articleService.update(articleSaveRequestDto, id);
        }
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id, HttpSession httpSession) {
        log.info("delete article delete request id={}", id);

        if (!isUserEqualsAuthor(id, httpSession)) {
            return "redirect:/";
        }
        if(!articleService.deleteById(id)) {
            return "redirect:/";
        }
        return "redirect:/";
    }

    private boolean isUserEqualsAuthor(long id, HttpSession httpSession) {
        Article article = articleService.findById(id);
        User user = (User) httpSession.getAttribute(USER);
        return article.isAuthor(user);
    }
}
