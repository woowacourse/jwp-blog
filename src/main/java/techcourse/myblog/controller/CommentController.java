package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.annotation.UserFromSession;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.dto.CommentDTO;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping
    public RedirectView create(@ModelAttribute CommentDTO commentDTO,
                               @PathVariable long articleId,
                               @UserFromSession User user) {

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
                               @UserFromSession User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
        comment.validate(user);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
        article.remove(comment);
        commentRepository.delete(comment);

        return new RedirectView("/articles/" + articleId);
    }

    @Transactional
    @PutMapping("/{commentId}")
    public RedirectView update(@PathVariable long articleId,
                               @PathVariable long commentId,
                               @ModelAttribute CommentDTO commentDTO,
                               @UserFromSession User user) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
        comment.validate(user);

        comment.update(commentDTO.toDomain(comment.getArticle(), comment.getAuthor()));

        return new RedirectView("/articles/" + articleId);
    }

    @ExceptionHandler(RuntimeException.class)
    public RedirectView exceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("commentError", exception.getMessage());

        log.error("error : {}", exception.getMessage());
        return new RedirectView(request.getHeader("Referer"));
    }

}
