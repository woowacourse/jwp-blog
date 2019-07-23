package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserPublicInfoDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.NotFoundUserException;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private ArticleService articleService;
    private UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage(HttpServletRequest httpServletRequest) {
        getLoggedInUser(httpServletRequest);
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleDto = articleService.findById(id);
        model.addAttribute("article", articleDto);

        UserPublicInfoDto userPublicInfoDto = userService.findById(articleDto.getUserId());
        model.addAttribute("articleUser", userPublicInfoDto);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest) {
        ArticleDto articleDto = articleService.findById(id);
        if (getLoggedInUser(httpServletRequest).getId().equals(articleDto.getUserId())) {
            model.addAttribute("article", articleDto);
            return "article-edit";
        }
        return "redirect:/login";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        articleDto.setUserId(getLoggedInUser(httpServletRequest).getId());
        ArticleDto savedArticleDto = articleService.save(articleDto);
        return "redirect:/articles/" + savedArticleDto.getId();
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        articleService.update(id, getLoggedInUser(httpServletRequest).getId(), articleDto);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id, HttpServletRequest httpServletRequest) {
        articleService.delete(id, getLoggedInUser(httpServletRequest).getId());
        return "redirect:/";
    }

    private UserPublicInfoDto getLoggedInUser(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserPublicInfoDto user = (UserPublicInfoDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (user == null) {
            throw new NotLoggedInException();
        }
        return user;
    }
}
