package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserProfileDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.exception.AccessNotPermittedException;
import techcourse.myblog.service.exception.NotFoundArticleException;
import techcourse.myblog.service.exception.NotFoundUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static techcourse.myblog.service.exception.AccessNotPermittedException.PERMISSION_FAIL_MESSAGE;

@Controller
public class ArticleController {
    private static final String LOGGED_IN_USER = "loggedInUser";

    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    public ArticleController(final ArticleRepository articleRepository, final UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage() {
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundArticleException::new);
        ArticleDto articleDto = new ArticleDto(article.getId(), article.getTitle(),
                article.getCoverUrl(), article.getContents());
        model.addAttribute("article", articleDto);

        User user = userRepository.findById(article.getUserId())
                .orElseThrow(NotFoundUserException::new);
        UserProfileDto userProfileDto = new UserProfileDto(user.getId(), user.getName(), user.getEmail());
        model.addAttribute("articleUser", userProfileDto);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model, HttpServletRequest httpServletRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(NotFoundArticleException::new);

        validateAuthor(httpServletRequest.getSession(), article);
        ArticleDto articleDto = new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents()
        );
        model.addAttribute("article", articleDto);
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserProfileDto userProfileDto = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);

        articleDto.setUserId(userProfileDto.getId());
        Article persistArticle = articleRepository.save(articleDto.toEntity());
        return "redirect:/articles/" + persistArticle.getId();
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        articleRepository.findById(id).ifPresent(article -> {
            validateAuthor(httpServletRequest.getSession(), article);
            article.updateArticle(articleDto);
            articleRepository.save(article);
        });
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id, HttpServletRequest httpServletRequest) {
        articleRepository.findById(id).ifPresent(article -> {
            validateAuthor(httpServletRequest.getSession(), article);
            articleRepository.deleteById(id);
        });
        return "redirect:/";
    }

    private void validateAuthor(HttpSession httpSession, Article article) {
        UserProfileDto user = (UserProfileDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (!article.matchUserId(user.getId())) {
            throw new AccessNotPermittedException(PERMISSION_FAIL_MESSAGE);
        }
    }
}
