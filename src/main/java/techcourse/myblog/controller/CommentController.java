package techcourse.myblog.controller;

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

    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{commentId}")
    public String updateForm(@PathVariable long articleId,
                             @PathVariable long commentId,
                             Model model) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundCommentException("존재하지 않는 댓글입니다."));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        model.addAttribute("article", article);
        model.addAttribute("editCommentId", commentId);
        model.addAttribute("comments", article.getComments());
        model.addAttribute("comment", comment);

        return "article";
    }

    @PostMapping("/")
    public RedirectView create(@ModelAttribute CommentDTO commentDTO,
                               @PathVariable long articleId,
                               HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            throw new UnauthenticatedUserException("로그인하지 않으면 댓글을 작성할 수 없습니다.");
        }

        Comment comment = commentDTO.toDomain();
        comment.setAuthor(user);

        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        comment.setArticle(article);
        article.add(comment);

        commentRepository.save(comment);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView delete(@PathVariable long articleId,
                               @PathVariable long commentId,
                               HttpSession httpSession) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundCommentException("해당 덧글이 존재하지 않습니다."));

        validateUser(httpSession, comment);

        article.remove(comment);
        commentRepository.delete(comment);

        return new RedirectView("/articles/" + articleId);
    }

    private void validateUser(HttpSession httpSession, Comment comment) {
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            throw new UnauthenticatedUserException("확인되지 않은 유저입니다.");
        }

        if (!user.equals(comment.getAuthor())) {
            throw new UnauthenticatedUserException("권한이 없습니다.");
        }
    }

    @PutMapping("/{commentId}")
    public RedirectView update(@PathVariable long articleId,
                               @PathVariable long commentId,
                               @ModelAttribute CommentDTO commentDTO,
                               HttpSession httpSession) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->
                        new NotFoundCommentException("해당 덧글이 존재하지 않습니다."));

        validateUser(httpSession, comment);

        comment.setContents(commentDTO.getContents());
        commentRepository.save(comment);

        return new RedirectView("/articles/" + articleId);
    }

    @ExceptionHandler(RuntimeException.class)
    public RedirectView exceptionHandler(RuntimeException exception, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        redirectAttributes.addFlashAttribute("commentError", exception.getMessage());

        return new RedirectView(request.getHeader("Referer"));
    }

}
