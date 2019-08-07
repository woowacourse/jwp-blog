package techcourse.myblog.article.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.article.dto.ArticleRequest;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.web.argumentResolver.AccessUserInfo;

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
    public String createArticle(final ArticleRequest articleDTO, final AccessUserInfo accessUserInfo) {
        Long id = articleService.save(articleDTO, accessUserInfo.getUser().getId());
        return "redirect:/articles/" + id;
    }

    @GetMapping("/{id}")
    public String showArticle(@PathVariable final Long id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/{id}")
    public String updateArticle(@PathVariable final Long id, final ArticleRequest article, final AccessUserInfo accessUserInfo) {
        articleService.update(id, article, accessUserInfo.getUser());
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable final Long id, final AccessUserInfo accessUserInfo) {
        articleService.delete(id, accessUserInfo.getUser());
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public ModelAndView showEditPage(@PathVariable final Long id, final AccessUserInfo accessUserInfo) {
        ModelAndView modelAndView = new ModelAndView();
        if (!accessUserInfo.match(articleService.findAuthor(id))) {
            modelAndView.setView(new RedirectView("/articles/" + id));
            return modelAndView;
        }
        modelAndView.addObject("articleDTO", articleService.findById(id));
        modelAndView.setViewName("article-edit");
        return modelAndView;
    }
}
