package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.ArticleRepository;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.user.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(ArticleDto articleDto, User author) {
        Article article = articleDto.toEntity(author);

        return articleRepository.save(article);
    }

    public Article findArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(NotFoundObjectException::new);
    }

    @Transactional
    public Article updateArticle(Long articleId, Article updatedArticle) {
        Article article = findArticle(articleId);
        article.update(updatedArticle);

        return article;
    }

    public void deleteArticle(Long articleId, User user) {
        Article article = findArticle(articleId);
        article.checkCorrespondingAuthor(user);
        articleRepository.deleteById(articleId);
    }

    public List<Comment> findAllComments(Article article) {
        return article.getComments();
    }

    @Transactional
    public Article addComment(Long articleId, Comment comment) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundObjectException::new);
        article.addComment(comment);
        return article;
    }

    public void checkAvailableUpdateUser(Article article, User user) {
        article.checkCorrespondingAuthor(user);
    }
}
