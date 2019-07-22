package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleInputException;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.translator.ArticleTranslator;
import techcourse.myblog.translator.ModelTranslator;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;
    private final ModelTranslator<Article, ArticleDto> articleTranslator;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        this.articleTranslator = new ArticleTranslator();
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView createArticle(@Valid ArticleDto articleDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new ArticleInputException("입력값이 잘못되었습니다.");
        }

        Article article = articleTranslator.toEntity(new Article(), articleDto);
        Article written = articleRepository.save(article);
        return new RedirectView("/articles/" + written.getId());
    }

    @GetMapping({"/articles/{articleId}", "/articles/{articleId}/edit"})
    public String readArticle(@PathVariable Long articleId, HttpServletRequest req, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("존재하지 않는 게시글 입니다."));
        model.addAttribute("article", article);

        if (req.getRequestURI().contains("edit")) {
            return "article-edit";
        }
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable Long articleId, ArticleDto articleDto, Errors errors) {
        if (errors.hasErrors()) {
            throw new ArticleInputException("입력값이 잘못되었습니다.");
        }

        Article updateArticle = articleTranslator.toEntity(articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException("존재하지 않는 게시글 입니다.")), articleDto);

        articleRepository.save(updateArticle);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("존재하지 않는 게시글 입니다."));
        articleRepository.delete(article);
        return new RedirectView("/");
    }

    @ExceptionHandler({ArticleNotFoundException.class, ArticleInputException.class})
    public RedirectView articleException(Exception exception) {
        return new RedirectView("/");
    }
}