package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.service.article.ArticleService;
import techcourse.myblog.service.dto.article.ArticleDto;
import techcourse.myblog.service.dto.user.UserResponseDto;

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
        List<ArticleDto> articleDtos = articleService.findAll();
        model.addAttribute("articleDtos", articleDtos);
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (!Objects.isNull(user)) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public ModelAndView createArticle(final ArticleDto articleDTO, final HttpSession session) {
        Long id = articleService.save(articleDTO, ((UserResponseDto) session.getAttribute(USER_SESSION_KEY)).getId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + id));
        return modelAndView;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable final Long id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public ModelAndView updateArticle(@PathVariable final Long id, final ArticleDto articleDTO, final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (!user.getId().equals(articleService.findAuthor(id).getId())) {
            modelAndView.setView(new RedirectView("/articles/" + id));
            return modelAndView;
        }
        articleService.update(id, articleDTO);
        modelAndView.setView(new RedirectView("/articles/" + id));
        return modelAndView;
    }

    @DeleteMapping("/articles/{id}")
    public ModelAndView deleteArticle(@PathVariable final Long id, final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (!user.getId().equals(articleService.findAuthor(id).getId())) {
            modelAndView.setView(new RedirectView("/articles/" + id));
            return modelAndView;
        }
        articleService.delete(id);
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }

    @GetMapping("/articles/{id}/edit")
    public ModelAndView showEditPage(@PathVariable final Long id, final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponseDto user = (UserResponseDto) session.getAttribute(USER_SESSION_KEY);
        if (!user.getId().equals(articleService.findAuthor(id).getId())) {
            modelAndView.setView(new RedirectView("/articles/" + id));
            return modelAndView;
        }
        modelAndView.addObject("articleDTO", articleService.findById(id));
        modelAndView.setViewName("article-edit");
        return modelAndView;
    }
}
