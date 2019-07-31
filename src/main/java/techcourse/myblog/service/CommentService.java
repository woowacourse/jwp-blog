package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;

@Slf4j
@Service
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }


    public Article findArticleByArticleId(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
