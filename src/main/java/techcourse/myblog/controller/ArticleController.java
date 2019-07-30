package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.UnauthenticatedUserException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{articleId}")
    public String show(@PathVariable("articleId") Long articleId, HttpSession session, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new NotFoundArticleException("게시물이 없습니다"));

        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());

        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        return "article";
    }

    @GetMapping("/new")
    public String createForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new UnauthenticatedUserException("로그인이 필요합니다.");
        }
        model.addAttribute("user", user);
        return "article-edit";
    }

    @PostMapping("/write")
    public String create(ArticleDTO articleDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            throw new UnauthenticatedUserException("로그인이 필요합니다.");
        }

        Article article = articleDTO.toDomain();
        article.setAuthor(user);

        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}/edit")
    public Object updateForm(@PathVariable("articleId") Long articleId, HttpSession session, Model model) {
        Article savedArticle = articleRepository
                .findById(articleId)
                .orElseThrow(() ->
                        new NotFoundArticleException("게시글이 존재하지 않습니다."));

        User user = (User) session.getAttribute("user");
        if (!user.equals(savedArticle.getAuthor())) {
            throw new UnauthenticatedUserException("수정 권한이 없습니다.");
        }

        model.addAttribute("article", savedArticle);
        model.addAttribute("articleId", articleId);
        return "article-edit";
    }

    @Transactional
    @PutMapping("/{articleId}")
    public String update(@PathVariable("articleId") Long articleId, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleId).get();
        article.update(articleDTO.toDomain());

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String delete(@PathVariable("articleId") Long articleId, HttpSession session) {
        Article savedArticle = articleRepository
                .findById(articleId)
                .orElseThrow(() ->
                        new NotFoundArticleException("게시글이 존재하지 않습니다."));

        User user = (User) session.getAttribute("user");
        if (!user.equals(savedArticle.getAuthor())) {
            throw new UnauthenticatedUserException("삭제 권한이 없습니다.");
        }

        articleRepository.deleteById(articleId);
        return "redirect:/";
    }

    @ExceptionHandler(RuntimeException.class)
    public RedirectView exceptionHandler(RuntimeException e, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("articleError", e.getMessage());
        return new RedirectView(request.getHeader("Referer"));
    }
}