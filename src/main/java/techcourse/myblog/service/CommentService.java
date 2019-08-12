package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.ArticleRepository;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.dto.CommentResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public List<CommentResponse> getComments(final long articleId) {
        Article article = getArticle(articleId);
        List<Comment> comments = article.getComments();
        return comments.stream()
                .map(x -> new CommentResponse(x.getAuthor().getUserName(), x.getContents(), x.getId()))
                .collect(Collectors.toList());
    }

    public CommentResponse save(final CommentDto commentDto, final long articleId, final User loginUser) {
        Article article = getArticle(articleId);
        Comment comment = commentDto.toDomain(article, loginUser);
        commentRepository.save(comment);
        article.add(comment);
        return new CommentResponse(article.getAuthor().getUserName(), comment.getContents(), comment.getId());
    }

    public void delete(final long commentId, final long articleId, final User loginUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
        comment.validate(loginUser);
        getArticle(articleId).remove(comment);
        commentRepository.delete(comment);
    }

    public Comment update(final long commentId, final CommentDto commentDto, final User loginUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NotFoundCommentException::new);
        comment.validate(loginUser);
        comment.update(commentDto.toDomain(comment.getArticle(), comment.getAuthor()));
        return comment;
    }

    public Article getArticle(final long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NotFoundArticleException::new);
    }

}
