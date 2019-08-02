package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.annotation.UserFromSession;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.UnauthenticatedUserException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{articleId}")
    public String show(@PathVariable Long articleId, @UserFromSession Optional<User> user, Model model) {
        Article article = articleRepository
                .findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());

        user.ifPresent(value -> model.addAttribute("user", value));

        return "article";
    }

    @GetMapping("/new")
    public String createForm(HttpSession session, @UserFromSession Optional<User> user,
                             Model model) {
        model.addAttribute("user", user.orElseThrow(UnauthenticatedUserException::new));
        return "article-edit";
    }

    @PostMapping
    public String create(ArticleDTO articleDTO, @UserFromSession Optional<User> user) {
        Article article = articleDTO.toDomain(user.orElseThrow(UnauthenticatedUserException::new));

        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}/edit")
    public Object updateForm(@PathVariable Long articleId,
                             @UserFromSession Optional<User> optionalUser,
                             Model model) {
        Article savedArticle = articleRepository
                .findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        User user = optionalUser.orElseThrow(UnauthenticatedUserException::new);

        checkAuthor(user, savedArticle);

        model.addAttribute("user", user);
        model.addAttribute("article", savedArticle);
        model.addAttribute("articleId", articleId);
        return "article-edit";
    }

    @Transactional
    @PutMapping("/{articleId}")
    public String update(@PathVariable Long articleId, @UserFromSession Optional<User> optionalUser,
                         ArticleDTO articleDTO) {
        Article savedArticle = articleRepository
                .findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        User user = optionalUser.orElseThrow(UnauthenticatedUserException::new);

        checkAuthor(user, savedArticle);
        savedArticle.update(articleDTO.toDomain(savedArticle.getAuthor()));

        return "redirect:/articles/" + articleId;
    }

    private void checkAuthor(User user, Article savedArticle) {
        if (!user.equals(savedArticle.getAuthor())) {
            throw new UnauthenticatedUserException();
        }
    }

    @DeleteMapping("/{articleId}")
    public String delete(@PathVariable Long articleId, @UserFromSession Optional<User> user) {
        Article savedArticle = articleRepository
                .findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        checkAuthor(user.orElseThrow(UnauthenticatedUserException::new), savedArticle);


        articleRepository.deleteById(articleId);
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public RedirectView exceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("articleError", exception.getMessage());
        log.error("error: {}", exception);

        return new RedirectView(request.getHeader("Referer"));
    }
}