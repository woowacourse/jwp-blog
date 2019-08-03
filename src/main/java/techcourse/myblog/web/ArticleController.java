package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.article.ArticleService;
import techcourse.myblog.service.article.ArticleRequestDto;
import techcourse.myblog.service.article.ArticleResponseDto;
import techcourse.myblog.service.user.UserResponseDto;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@ControllerAdvice
@Controller
public class ArticleController {
    final private ArticleService articleService;

    @Autowired
    public ArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(Model model, final HttpSession session) {
        List<ArticleResponseDto> articleDtos = articleService.findAll();
        model.addAttribute("articleDtos", articleDtos);
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(final ArticleRequestDto articleDTO, final HttpSession session) {
        Long id = articleService.save(articleDTO, ((UserResponseDto) session.getAttribute(USER_SESSION_KEY)).getId());
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable final Long id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable final Long id, final ArticleRequestDto articleDTO, final HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (user.matchId(articleService.findAuthor(id).getId())) {
            articleService.update(id, articleDTO);
        }
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable final Long id, final HttpSession session) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (user.matchId(articleService.findAuthor(id).getId())) {
            articleService.delete(id);
            return "redirect:/";
        }
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable final Long id, final HttpSession session, Model model) {
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (!user.matchId(articleService.findAuthor(id).getId())) {
            return "redirect:/articles/" + id;
        }
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article-edit";
    }
}
