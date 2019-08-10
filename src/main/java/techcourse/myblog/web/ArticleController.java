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
@RequestMapping("/articles")
public class ArticleController {
    private ArticleService articleService;
    private UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAllWithCommentCount());
        return "index";
    }

    @GetMapping("/new")
    public String showCreatePage(LoginUserDto user) {
        return "article-edit";
    }

    @GetMapping("/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleDto = articleService.findArticleDtoById(id);
        model.addAttribute("article", articleDto);

        UserPublicInfoDto userPublicInfoDto = userService.findUserPublicInfoById(articleDto.getUserId());
        model.addAttribute("articleUser", userPublicInfoDto);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model, LoginUserDto user) {
        ArticleDto articleDto = articleService.findArticleDtoById(id);
        if (user.matchId(articleDto.getUserId())) {
            model.addAttribute("article", articleDto);
            return "article-edit";
        }
        return "redirect:/articles/" + id;
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto, LoginUserDto user) {
        ArticleDto savedArticleDto = articleService.save(user.getId(), articleDto);
        return "redirect:/articles/" + savedArticleDto.getId();
    }

    @PutMapping("/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto, LoginUserDto user) {
        articleService.update(id, user.getId(), articleDto);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable("id") long id, LoginUserDto user) {
        articleService.delete(id, user.getId());
        return "redirect:/";
    }
}
