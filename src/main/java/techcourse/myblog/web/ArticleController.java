package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.service.article.ArticleService;
import techcourse.myblog.service.dto.article.ArticleRequest;
import techcourse.myblog.service.dto.user.UserResponse;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.user.UserService.USER_SESSION_KEY;

@ControllerAdvice
@Controller
@RequestMapping("/articles")
public class ArticleController {
    final private ArticleService articleService;

    @Autowired
    public ArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("")
    public String createArticle(final ArticleRequest articleDTO, final HttpSession session) {
        Long id = articleService.save(articleDTO, ((UserResponse) session.getAttribute(USER_SESSION_KEY)).getId());
        return "redirect:/articles/" + id;
    }

    @GetMapping("/{id}")
    public String showArticle(@PathVariable final Long id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/{id}")
    public String updateArticle(@PathVariable final Long id, final ArticleRequest articleDTO, final HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (!user.getId().equals(articleService.findAuthor(id).getId())) {
            return "redirect:/articles/" + id;
        }
        articleService.update(id, articleDTO, user);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable final Long id, final HttpSession session) {
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (!user.getId().equals(articleService.findAuthor(id).getId())) {
            return "redirect:/articles/" + id;
        }
        articleService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditPage(@PathVariable final Long id, final HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        UserResponse user = (UserResponse) session.getAttribute(USER_SESSION_KEY);
        if (!user.getId().equals(articleService.findAuthor(id).getId())) {
            modelAndView.setView(new RedirectView("/articles/" + id));
            return modelAndView;
        }
        modelAndView.addObject("articleDTO", articleService.findById(id));
        modelAndView.setViewName("article-edit");
        return modelAndView;
    }
}
