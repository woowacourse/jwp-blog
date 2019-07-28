package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.exception.*;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public List<Comment> findAll(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    public Comment save(Comment comment, User user, Long articleId) {
        Article article = articleRepository
                .findById(articleId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 게시글"));
        comment.setUser(user);
        comment.setArticle(article);
        return commentRepository.save(comment);
    }

    public void delete(long commentId, User user) {
        Comment comment = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException("존재하지 않는 댓글"));

        if (!user.equals(comment.getUser())) {
            throw new IllegalRequestException("권한이 없는 사용자 입니다.");
        }

        commentRepository.delete(comment);
    }
}
