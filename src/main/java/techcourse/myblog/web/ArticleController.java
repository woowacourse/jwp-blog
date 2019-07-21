package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.web.dto.ArticleRequestDto;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static techcourse.myblog.web.LoginUtil.SESSION_USER_KEY;
import static techcourse.myblog.web.LoginUtil.checkAndPutUser;

@Controller
public class ArticleController {

    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String indexView(Model model, @SessionAttribute(SESSION_USER_KEY) Optional<User> currentUser) {
        checkAndPutUser(model, currentUser);
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticleView(Model model, HttpSession session) {
        model.addAttribute(SESSION_USER_KEY, session.getAttribute(SESSION_USER_KEY));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String publishArticle(ArticleRequestDto article) {
        Article saved = articleRepository.save(Article.from(article));
        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{articleId}")
    public String articleView(@PathVariable Long articleId, Model model) {
        try {
            Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> createNotFoundException(articleId));
            model.addAttribute("article", article);
            return "article";
        } catch (NoSuchElementException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticleView(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> createNotFoundException(articleId));
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable Long articleId, ArticleRequestDto reqArticle, Model model) {
        Article article = Article.of(articleId, reqArticle.getTitle(), reqArticle.getCoverUrl(), reqArticle.getContents());
        articleRepository.save(article);
        Article articleToShow = articleRepository.findById(articleId)
            .orElseThrow(() -> createNotFoundException(articleId));
        model.addAttribute("article", articleToShow);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }

    private static NoSuchElementException createNotFoundException(Long id) {
        return new NoSuchElementException("Can't find article with id: " + id);
    }
}
