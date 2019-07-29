package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles")
public class CommentController {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentController(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @PostMapping("/{articleId}/comments")
    public RedirectView create(@ModelAttribute CommentDTO commentDTO,
                               @PathVariable long articleId,
                               HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            throw new UnauthenticatedUserException("로그인하지 않으면 댓글을 작성할 수 없습니다.");
        }

        Comment comment = commentDTO.toDomain();
        comment.setAuthor(user);

        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new NotFoundArticleException("게시글을 찾을 수 없습니다.")
        );

        comment.setArticle(article);
        article.add(comment);

        commentRepository.save(comment);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}/comments/{commentId}")
    public RedirectView delete(@PathVariable long articleId,
                               @PathVariable long commentId,
                               HttpSession httpSession) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new NotFoundArticleException("article is not found"));

        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundCommentException("해당 덧글이 존재하지 않습니다."));

        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            throw new UnauthenticatedUserException("확인되지 않은 유저입니다.");
        }

        if (!user.equals(comment.getAuthor())) {
            throw new UnauthenticatedUserException("권한이 없습니다.");
        }

        article.remove(comment);
        commentRepository.delete(comment);

        return new RedirectView("/articles/" + articleId);
    }

}
