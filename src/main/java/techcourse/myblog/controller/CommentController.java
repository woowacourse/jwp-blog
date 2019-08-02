package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.exception.UnauthenticatedUserException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.dto.CommentDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{commentId}")
    public String updateForm(@PathVariable long articleId,
                             @PathVariable long commentId,
                             Model model) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        model.addAttribute("article", article);
        model.addAttribute("editCommentId", commentId);
        model.addAttribute("comments", article.getComments());
        model.addAttribute("comment", comment);

        return "article";
    }

    @PostMapping
    public RedirectView create(@ModelAttribute CommentDTO commentDTO,
                               @PathVariable long articleId,
                               HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            throw new UnauthenticatedUserException();
        }

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        Comment comment = commentDTO.toDomain(article, user);

        article.add(comment);
        commentRepository.save(comment);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView delete(@PathVariable long articleId,
                               @PathVariable long commentId,
                               HttpSession httpSession) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);

        validateUser(httpSession, comment);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        article.remove(comment);
        commentRepository.delete(comment);

        return new RedirectView("/articles/" + articleId);
    }

    private void validateUser(HttpSession httpSession, Comment comment) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            throw new UnauthenticatedUserException();
        }

        if (!user.equals(comment.getAuthor())) {
            throw new UnauthenticatedUserException();
        }
    }

    @PutMapping("/{commentId}")
    public RedirectView update(@PathVariable long articleId,
                               @PathVariable long commentId,
                               @ModelAttribute CommentDTO commentDTO,
                               HttpSession httpSession) {

        Comment savedComment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);

        validateUser(httpSession, savedComment);

        savedComment.update(commentDTO.toDomain(savedComment.getArticle(), savedComment.getAuthor()));
        commentRepository.save(savedComment);

        return new RedirectView("/articles/" + articleId);
    }

    @ExceptionHandler(RuntimeException.class)
    public RedirectView exceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("commentError", exception.getMessage());

        log.error("error : {}", exception.getMessage());
        return new RedirectView(request.getHeader("Referer"));
    }

}
