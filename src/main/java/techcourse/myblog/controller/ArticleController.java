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
    public static final String ARTICLE_URL = "/articles";
    private ArticleRepository articleRepository;
    private CommentRepository commentRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping()
    public String saveArticlePage(HttpSession httpSession, ArticleDto articleDto) {
        log.debug(">>> save article : {}", articleDto);
        User user = (User) httpSession.getAttribute("user");

        Article article = articleRepository.save(articleDto.toArticle(user));
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, HttpSession httpSession, Model model) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleRepository.findById(articleId).get();
        if (isAuthor(article, httpSession)) return "redirect:/";
        model.addAttribute("article", article);
        return "article-edit";
    }

    @GetMapping("{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleRepository.findById(articleId).get();
        log.debug(">>> get article : {}", article);
        List<Comment> comments = commentRepository.findAllByArticle_Id(article.getId());
        log.debug(">>> get article comment size : {}", comments.size());
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @PutMapping("{articleId}")
    public String updateArticleByIdPage(HttpSession httpSession, ArticleDto articleDto) {
        log.debug(">>> put ArticleDto : {}", articleDto);
        User user = (User) httpSession.getAttribute("user");
        Article preArticle = articleRepository.findById(articleDto.getId()).get();
        log.debug(">>> put Article before save: {}", articleDto);

        if (isAuthor(preArticle, httpSession)) return "redirect:/";

        Article article = articleRepository.save(articleDto.toArticle(user));
        log.debug(">>> put Article : {}", article);
        return "redirect:/articles/" + article.getId();
    }

    @Transactional
    @DeleteMapping("{articleId}")
    public String deleteArticleByIdPage(@PathVariable long articleId, HttpSession httpSession) {
        log.debug(">>> article Id : {}", articleId);
        Article article = articleRepository.findById(articleId).get();
        log.debug(">>> before delete comment size : {}", commentRepository.findAllByArticle_Id(articleId).size());
        if (isAuthor(article, httpSession)) return "redirect:/";
        articleRepository.deleteById(articleId);
        commentRepository.deleteAllByArticle(article);
        log.debug(">>> after delete comment size : {}", commentRepository.findAllByArticle_Id(articleId).size());
        return "redirect:/";
    }

    private boolean isAuthor(Article article, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute("user");
        if (article.getAuthor().getId() != author.getId()) {
            return true;
        }
        return false;
    }

}
