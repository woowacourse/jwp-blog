package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.user.UserDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CategoryService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @GetMapping("/articles/new")
    public String showArticleWritingPage(Model model) {
        model.addAttribute("categories", categoryService.readAll());
        return "article-edit";
    }

    @PostMapping("/articles/new")
    public String create(ArticleDto articleDto, HttpSession session) {
        Object userId = session.getAttribute("userId");
        long articleId = articleService.createArticle(articleDto, (long) userId);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable long articleId, Model model) {
        ArticleDto articleDto = articleService.readById(articleId);
        model.addAttribute("article", articleDto);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable long articleId, ArticleDto articleDto, HttpSession session) {
        Object userId = session.getAttribute("userId");
        articleService.checkAuthor(articleId, (long) userId);
        ArticleDto toArticleDto = articleService.updateByArticle(articleId, articleDto);

        return "redirect:/articles/" + toArticleDto.getId();
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditPage(@PathVariable long articleId, Model model) {
        ArticleDto articleDto = articleService.readById(articleId);

        model.addAttribute("article", articleDto);
        model.addAttribute("categories", categoryService.readAll());
        return "article-edit";
    }

    @DeleteMapping("articles/{articleId}")
    public String delete(@PathVariable long articleId, HttpSession session) {
        Object userId = session.getAttribute("userId");
        articleService.checkAuthor(articleId, (long) userId);
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
