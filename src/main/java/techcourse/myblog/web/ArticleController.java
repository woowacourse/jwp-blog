package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.service.ArticleService;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(Model model, final HttpSession session) {
        List<ArticleDto> articleDtos = articleService.findAll();
        model.addAttribute("articleDTOs", articleDtos);
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
    public ModelAndView createArticle(ArticleDto articleDTO) {
        int id = articleService.save(articleDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + id));
        return modelAndView;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public ModelAndView updateArticle(@PathVariable int id, ArticleDto articleDTO) {
        articleService.update(id, articleDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/articles/" + id));
        return modelAndView;
    }

    @DeleteMapping("/articles/{id}")
    public ModelAndView deleteArticle(@PathVariable int id) {
        articleService.delete(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/"));
        return modelAndView;
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article-edit";
    }
}
