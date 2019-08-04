package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;
import java.util.List;

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
        List<CommentDto.Response> comments = commentService.findAllByArticle(articleId);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable Long articleId, HttpSession httpSession, Model model) {
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        model.addAttribute("article", articleService.findById(userDto, articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto.Update articleDto, HttpSession httpSession) {
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        ArticleDto.Response updatedArticle = articleService.update(userDto, articleId, articleDto);
        return "redirect:/articles/" + updatedArticle.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
        UserDto.Response userDto = (UserDto.Response) httpSession.getAttribute("user");
        articleService.deleteById(userDto, articleId);
        return "redirect:/";
    }
}
