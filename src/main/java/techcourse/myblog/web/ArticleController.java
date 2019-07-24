package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleAssembler;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleWriteDto;

@Controller(value = "/articles")
@RequiredArgsConstructor
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping
    public String writeArticle(ArticleWriteDto articleWriteDto, Model model) {
        Article article = articleRepository.save(ArticleAssembler.writeArticle(articleWriteDto));
        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/edit/{articleId}")
    public String showArticleEditingPage(@PathVariable int articleId, Model model) {
        model.addAttribute("article",
                articleRepository.findById(articleId).orElseThrow(() -> new ArticleException("잘못된 접근입니다.")));
        return "article-edit";
    }

    @GetMapping("/{articleId}")
    public String showArticleById(@PathVariable int articleId, Model model) {
        model.addAttribute("article",
                articleRepository.findById(articleId).orElseThrow(() -> new ArticleException("잘못된 접근입니다.")));
        return "article";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable int articleId, ArticleWriteDto articleWriteDto, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.update(ArticleAssembler.writeArticle(articleWriteDto));
        articleRepository.save(article);
        model.addAttribute("article", article);
        return "article";
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }

    @ExceptionHandler
    public String handleArticleException(ArticleException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", e.getMessage());
        return "redirect:/err";
    }
}
