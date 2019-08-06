package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.web.exception.NotLoggedInException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ArticleController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private ArticleService articleService;
    private UserService userService;
    private CommentService commentService;

    public ArticleController(ArticleService articleService, UserService userService, CommentService commentService) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAllWithCommentCount());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage(HttpServletRequest httpServletRequest) {
        getLoggedInUser(httpServletRequest);
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
    public String showEditPage(@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest) {
        ArticleDto articleDto = articleService.findArticleDtoById(id);
        if (getLoggedInUser(httpServletRequest).getId().equals(articleDto.getUserId())) {
            model.addAttribute("article", articleDto);
            return "article-edit";
        }
        return "redirect:/articles/" + id;
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        ArticleDto savedArticleDto = articleService.save(getLoggedInUser(httpServletRequest).getId(), articleDto);
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
