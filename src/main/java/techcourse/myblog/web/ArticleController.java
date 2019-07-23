package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.article.ArticleDto;
import techcourse.myblog.dto.user.UserResponseDto;
import techcourse.myblog.service.article.ArticleService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

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
        UserResponseDto user = (UserResponseDto) session.getAttribute("user");
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
    public ModelAndView createArticle(final ArticleDto articleDTO) {
        int id = articleService.save(articleDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + id));
        return modelAndView;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable final int id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public ModelAndView updateArticle(@PathVariable final int id, final ArticleDto articleDTO) {
        articleService.update(id, articleDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + id));
        return modelAndView;
    }

    @DeleteMapping("/articles/{id}")
    public ModelAndView deleteArticle(@PathVariable final int id) {
        articleService.delete(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable final int id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article-edit";
    }
}
