package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.*;
import techcourse.myblog.domain.assembler.ArticleAssembler;
import techcourse.myblog.domain.assembler.CommentAssembler;
import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.ArticleResponse;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public List<ArticleResponse> getAllArticles() {
        return ArticleAssembler.writeDtos(articleRepository.findAll());
    }

    public ArticleResponse getArticleDtoById(long articleId) {
        Article article = getArticleById(articleId);
        return ArticleAssembler.writeDto(article);
    }

    public Article getArticleById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NoSuchElementException::new);
    }

    public Article save(ArticleRequest articleDto, User user) {
        Article article = ArticleAssembler.writeArticle(articleDto, user);
        return articleRepository.save(article);
    }

    @Transactional
    public Article update(long articleId, ArticleRequest articleDto, User user) {
        Article article = getArticleById(articleId);
        article.checkCorrespondingAuthor(user);
        Article updatedArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), user);
        return article.update(updatedArticle);
    }

    public void deleteById(long articleId, User user) {
        Article article = getArticleById(articleId);
        article.checkCorrespondingAuthor(user);
        articleRepository.deleteById(articleId);
    }

    public List<CommentResponse> getAllComments(long articleId) {
        Article article = getArticleById(articleId);
        return CommentAssembler.writeDtos(article.getComments());
    }

    public Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NoSuchElementException::new);
    }

    public Comment saveComment(long articleId, CommentRequest commentDto, User user) {
        Article article = getArticleById(articleId);
        Comment comment = CommentAssembler.writeComment(commentDto, user);
        article.saveComment(comment);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(long commentId, CommentRequest commentDto, User user) {
        Comment comment = getCommentById(commentId);
        comment.checkCorrespondingAuthor(user);
        Comment updatedComment = CommentAssembler.writeComment(commentDto, user);
        return comment.update(updatedComment);
    }

    public void deleteComment(long commentId, User user) {
        Comment comment = getCommentById(commentId);
        comment.checkCorrespondingAuthor(user);
        commentRepository.deleteById(commentId);
    }
}
