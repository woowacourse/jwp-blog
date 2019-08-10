package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.LoginUserDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;

@Controller
public class ArticleController {
    private ArticleService articleService;
    private UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAllWithCommentCount());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage(LoginUserDto user) {
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleDto = articleService.findArticleDtoById(id);
        model.addAttribute("article", articleDto);

        UserPublicInfoDto userPublicInfoDto = userService.findUserPublicInfoById(articleDto.getUserId());
        model.addAttribute("articleUser", userPublicInfoDto);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model, LoginUserDto user) {
        ArticleDto articleDto = articleService.findArticleDtoById(id);
        if (user.matchId(articleDto.getUserId())) {
            model.addAttribute("article", articleDto);
            return "article-edit";
        }
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, LoginUserDto user) {
        ArticleDto savedArticleDto = articleService.save(user.getId(), articleDto);
        return "redirect:/articles/" + savedArticleDto.getId();
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto, LoginUserDto user) {
        articleService.update(id, user.getId(), articleDto);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id, LoginUserDto user) {
        articleService.delete(id, user.getId());
        return "redirect:/";
    }
}
