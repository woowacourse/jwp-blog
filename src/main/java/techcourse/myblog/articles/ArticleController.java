package techcourse.myblog.articles;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.articles.comments.Comment;
import techcourse.myblog.articles.comments.CommentService;
import techcourse.myblog.users.UserSession;

import java.util.List;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/new")
    public String writeForm() {
        return "article-edit";
    }

    @PostMapping
    public String write(UserSession userSession, Article article) {
        Long userId = userSession.getId();
        Article savedArticle = articleService.save(userId, article);

        return "redirect:/articles/" + savedArticle.getId();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        final Article article = articleService.findById(id);
        final List<Comment> comments = article.getComments();

        model.addAttribute(article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, UserSession userSession, Model model) {
        Long userId = userSession.getId();
        Article article = articleService.findById(userId, id);

        model.addAttribute(article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String edit(UserSession userSession, Article editedArticle) {
        Long userId = userSession.getId();

        Article article = articleService.edit(userId, editedArticle);

        return "redirect:/articles/" + article.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, UserSession userSession) {
        Long userId = userSession.getId();

        articleService.deleteById(userId, id);
        return "redirect:/";
    }
}
