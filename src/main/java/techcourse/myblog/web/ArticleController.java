package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage(HttpSession httpSession, Model model) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userSession.isPresent()) {
            model.addAttribute("user", userSession.get());
        }
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Create articleDto, HttpSession httpSession) {
        long newArticleId = articleService.save(articleDto);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, HttpSession httpSession, Model model) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userSession.isPresent()) {
            model.addAttribute("user", userSession.get());
        }
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, HttpSession httpSession, Model model) {
        Optional<UserDto.Response> userSession = Optional.ofNullable((UserDto.Response) httpSession.getAttribute("user"));
        if (userSession.isPresent()) {
            model.addAttribute("user", userSession.get());
        }
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto.Update articleDto, HttpSession httpSession) {
        long updatedArticleId = articleService.update(articleId, articleDto);
        return "redirect:/articles/" + updatedArticleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId, HttpSession httpSession) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
