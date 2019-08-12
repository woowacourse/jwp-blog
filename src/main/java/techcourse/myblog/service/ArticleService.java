package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.article.Contents;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.assembler.ArticleAssembler;
import techcourse.myblog.service.exception.NotFoundObjectException;

import java.util.List;


@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Article createArticle(Contents contents, User author) {
        Article article = ArticleAssembler.toEntity(contents, author);
        return articleRepository.save(article);
    }

    public Article findArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NotFoundObjectException::new);
    }

    @Transactional
    public Article updateArticle(Long articleId, Contents contents, User user) {
        Article article = findArticle(articleId);
        Article newArticle = ArticleAssembler.toEntity(contents, user);
        article.update(newArticle);

        return article;
    }

    public void deleteArticle(Long articleId, User user) {
        Article article = findArticle(articleId);
        article.checkCorrespondingAuthor(user);
        deleteCommentsByArticleId(articleId);
        articleRepository.deleteById(articleId);
    }

    public void checkAvailableUpdateUser(Article article, User user) {
        article.checkCorrespondingAuthor(user);
    }

    public Article findById(Long id) {
        return articleRepository.findById(id).orElseThrow(NotFoundObjectException::new);
    }

    private void deleteCommentsByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleArticleId(articleId);

        for(Comment comment : comments) {
            commentRepository.delete(comment);
        }
    }
}
