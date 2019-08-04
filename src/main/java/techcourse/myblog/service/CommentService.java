package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.CommentDto;
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

    public Long save(CommentDto commentDto, User user) {
        Article foundArticle = findByArticleId(commentDto.getArticleId());
        Comment comment = new Comment(commentDto.getContents(), user, foundArticle);

        foundArticle.addComment(comment);
        commentRepository.save(comment);
        return foundArticle.getId();
    }

    private Article findByArticleId(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new ArticleNotFoundException("게시글을 찾을 수 없습니다."));
    }

    public Comment findById(Long commentId, User user) {
        return checkOwner(commentId, user);
    }

    @Transactional
    public Comment update(CommentDto commentDto, Long commentId, User user) {
        Comment oldComment = findById(commentId, user);
        Comment updatedComment = new Comment(commentDto.getContents(), oldComment.getAuthor(), oldComment.getArticle());
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
