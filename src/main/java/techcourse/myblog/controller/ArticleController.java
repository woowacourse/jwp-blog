package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.domain.*;

import javax.servlet.http.HttpSession;
import java.util.List;

import static techcourse.myblog.controller.ArticleController.ARTICLE_URL;

@Slf4j
@Controller
@RequestMapping(ARTICLE_URL)
public class ArticleController {
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;
    public static final String ARTICLE_URL = "/articles";

    @Autowired
    public ArticleController(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping
    public String saveArticlePage(ArticleDto articleDto, User user) {
        log.debug(">>> save article : {}, user : {}", articleDto, user);
        Article article = articleRepository.save(articleDto.toArticle(user));
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model, User user) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
        if (article.isNotMatchAuthor(user)) {
            return "redirect:/";
        }
        model.addAttribute("article", article);
        return "article-edit";
    }

    @GetMapping("{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
        List<Comment> comments = commentRepository.findAllByArticle_Id(article.getId());
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @PutMapping("{articleId}")
    public String updateArticleByIdPage(ArticleDto articleDto, User user) {
        log.debug(">>> put article ArticleDto : {}, user : {}", articleDto, user);
        Article preArticle = articleRepository.findById(articleDto.getId()).orElseThrow(RuntimeException::new);
        if (preArticle.isNotMatchAuthor(user)) {
            return "redirect:/";
        }
        Article article = articleRepository.save(articleDto.toArticle(user));
        return "redirect:/articles/" + article.getId();
    }

    @Transactional
    @DeleteMapping("{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId, User user) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
        if (article.isNotMatchAuthor(user)) {
            return "redirect:/";
        }
        articleRepository.deleteById(articleId);
        commentRepository.deleteAllByArticle(article);
        return "redirect:/";
    }
}
