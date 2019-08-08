package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;

@Slf4j
@Service
@Transactional
public class CommentRestService {
    private ArticleService articleService;
    private CommentService commentService;

    @Autowired
    public CommentRestService(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    public List<Comment> findAllByArticleId(Long articleId) {
        return commentService.findAllByArticleId(articleId);
    }

    public int getCommentSizeByArticleId(Long articleId) {
        return commentService.findCommentsByArticleId(articleId).size();
    }

    public CommentResponse save(CommentRequest commentRequest, User user) {
        Article article = articleService.select(commentRequest.getArticleId());
        Comment comment = new Comment(commentRequest.getContents(), user, article);
        Comment savedComment = commentService.save(comment);
        return new CommentResponse(savedComment, savedComment.getAuthor());
    }

    public CommentResponse put(Long commentId, CommentRequest commentRequest, User user) {
        Comment editedComment = commentService.update(commentId, commentRequest, user);
        return new CommentResponse(editedComment, editedComment.getAuthor());
    }

    public void delete(Long commentId, User user) {
        commentService.delete(commentId, user);
    }
}
