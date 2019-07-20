package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.UserPublicInfoDto;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);


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
    public String showCreatePage(HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        if (httpSession.getAttribute(LOGGED_IN_USER) == null) {
            return "redirect:/login";
        }
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id).get();
        ArticleDto articleDto = new ArticleDto(article.getId(), article.getTitle(),
                article.getCoverUrl(), article.getContents());
        model.addAttribute("article", articleDto);

        log.debug("Read Article User Id : {}", article.getUserId());
        User user = userRepository.findById(article.getUserId()).get();
        UserPublicInfoDto userPublicInfoDto = new UserPublicInfoDto(user.getId(), user.getName(), user.getEmail());
        model.addAttribute("user", userPublicInfoDto);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long id, Model model) {
        Article article = articleRepository.findById(id).get();
        ArticleDto articleDto = new ArticleDto(article.getId(), article.getTitle(), article.getCoverUrl(), article.getContents());
        model.addAttribute("article", articleDto);
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        UserPublicInfoDto userPublicInfoDto = (UserPublicInfoDto) httpSession.getAttribute(LOGGED_IN_USER);
        if (userPublicInfoDto == null) {
            return "redirect:/login";
        }
        log.debug("PublicUser Dto User Id : {}", userPublicInfoDto.getId());
        articleDto.setUserId(userPublicInfoDto.getId());
        Article persistArticle = articleRepository.save(articleDto.toEntity());
        log.debug("Create Article User Id : {}", persistArticle.getUserId());
        return "redirect:/articles/" + persistArticle.getId();
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, ArticleDto articleDto) {
        Article article = articleRepository.findById(id).get();
        article.updateArticle(articleDto);
        articleRepository.save(article);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id) {
        articleRepository.deleteById(id);
        return "redirect:/";
    }
}
