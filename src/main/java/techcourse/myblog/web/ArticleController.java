package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage(HttpSession httpSession, Model model) {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Create articleDto, HttpSession httpSession) {
        UserDto.Response user = (UserDto.Response) httpSession.getAttribute("user");

        Long newArticleId = articleService.save(user, articleDto);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable Long articleId, HttpSession httpSession, Model model) {

        model.addAttribute("article", articleService.findById(articleId));
        List<Comment> allByArticle = commentService.findAllByArticle(articleId);
        model.addAttribute("comments", allByArticle);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable Long articleId, HttpSession httpSession, Model model) {
        Optional<UserDto.Response> userDto = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userDto.isPresent()) {
            model.addAttribute("article", articleService.findById(userDto.get(), articleId));
        }
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto.Update articleDto, HttpSession httpSession) {
        Optional<UserDto.Response> userDto = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userDto.isPresent()) {
            Long updatedArticleId = articleService.update(userDto.get(), articleId, articleDto);
            return "redirect:/articles/" + updatedArticleId;
        }
        return null;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
        Optional<UserDto.Response> userDto = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userDto.isPresent()) {
            articleService.deleteById(userDto.get(), articleId);
            return "redirect:/";
        }
        return null;
    }
}
