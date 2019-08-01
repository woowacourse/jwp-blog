package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.*;
import techcourse.myblog.domain.assembler.ArticleAssembler;
import techcourse.myblog.domain.assembler.CommentAssembler;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
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

    public List<ArticleDto> getAllArticles() {
        return ArticleAssembler.writeDtos(articleRepository.findAll());
    }

    public ArticleDto getArticleDtoById(long articleId) {
        Article article = getArticleById(articleId);
        return ArticleAssembler.writeDto(article);
    }

    public Article getArticleById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NoSuchElementException::new);
    }

    public Article save(ArticleDto articleDto, User user) {
        Article newArticle = ArticleAssembler.writeArticle(articleDto, user);
        return articleRepository.save(newArticle);
    }

    @Transactional
    public Article update(ArticleDto articleDto, User user) {
        Article article = getArticleById(articleDto.getArticleId());
        article.checkCorrespondingAuthor(user);
        Article updatedArticle = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents(), user);
        return article.update(updatedArticle);
    }

    public void deleteById(long articleId, User user) {
        Article article = getArticleById(articleId);
        article.checkCorrespondingAuthor(user);
        articleRepository.deleteById(articleId);
    }

    public List<CommentDto> getAllComments(long articleId) {
        Article article = getArticleById(articleId);
        return CommentAssembler.writeDtos(article.getComments());
    }

    public Comment getCommentById(long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NoSuchElementException::new);
    }

    public Comment saveComment(long articleId, CommentDto commentDto, User user) {
        Article article = getArticleById(articleId);
        Comment comment = CommentAssembler.writeComment(commentDto, user);
        article.saveComment(comment);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, CommentDto commentDto, User user) {
        Comment comment = getCommentById(commentId);
        comment.checkCorrespondingAuthor(user);
        Comment updatedComment = CommentAssembler.writeComment(commentDto, user);
        return comment.update(updatedComment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = getCommentById(commentId);
        comment.checkCorrespondingAuthor(user);
        commentRepository.deleteById(commentId);
    }
}
