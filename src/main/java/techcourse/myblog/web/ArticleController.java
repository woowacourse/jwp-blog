package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleSaveRequestDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping
    public RedirectView saveArticle(ArticleSaveRequestDto articleSaveRequestDto, User user) {
        log.info("save article post request params={}", articleSaveRequestDto);

        Article article = articleService.save(articleSaveRequestDto, user);
        Long id = article.getId();

        return new RedirectView("/articles/" + id);
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
    public String editArticle(@PathVariable long id, Model model, User user) {
        Article article = articleService.findById(id);

        if (article.isNotAuthor(user)) {
            return "redirect:/articles/" + id;
        }
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public RedirectView saveEditedArticle(@PathVariable long id, ArticleSaveRequestDto articleSaveRequestDto, User user) {
        articleService.update(articleSaveRequestDto, id, user);
        log.info("save edited article post request params={}", articleSaveRequestDto);

        return new RedirectView("/articles/" + id);
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteArticle(@PathVariable long id, User user) {
        log.info("delete article delete request id={}", id);

        articleService.deleteById(id, user);

        return new RedirectView("/");
    }
}
