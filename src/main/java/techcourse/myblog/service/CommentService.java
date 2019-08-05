package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.ResponseCommentDto;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.exception.CommentNotFoundException;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public ResponseCommentDto save(RequestCommentDto requestCommentDto, User user) {
        Article foundArticle = findByArticleId(requestCommentDto.getArticleId());
        Comment comment = new Comment(requestCommentDto.getContents(), user, foundArticle);

        foundArticle.addComment(comment);
        commentRepository.save(comment);
        return new ResponseCommentDto(comment.getId(), comment.getAuthor().getUserName(), comment.getUpdateTime(), comment.getContents());
    }

    private Article findByArticleId(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public Comment findById(Long commentId, User user) {
        return checkOwner(commentId, user);
    }

    @Transactional
    public Comment update(RequestCommentDto requestCommentDto, Long commentId, User user) {
        Comment oldComment = findById(commentId, user);
        Comment updatedComment = new Comment(requestCommentDto.getContents(), oldComment.getAuthor(), oldComment.getArticle());
        return oldComment.update(updatedComment);
    }

    public Article delete(Long commentId, User user) {
        Comment comment = findById(commentId, user);
        Article deleteArticle = comment.getArticle();

        deleteArticle.deleteComment(comment);
        commentRepository.deleteById(commentId);

        return deleteArticle;
    }

    private Comment checkOwner(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글을 찾을수 없습니다."));
        comment.checkOwner(user);
        return comment;
    }
}
