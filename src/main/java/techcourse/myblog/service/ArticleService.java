package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.domain.*;

import java.util.List;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public long save(ArticleDto articleDto, long userId) {
        User author = getUserOrElseThrow(userId);
        Article newArticle = articleDto.toArticle(author);
        Article savedArticle = articleRepository.save(newArticle);
        return savedArticle.getId();
    }

    public Article update(ArticleDto articleDto, long userId) {
        User user = getUserOrElseThrow(userId);
        Article article = articleDto.toArticle(user);
        articleRepository.save(article);
        return article;
    }

    public void delete(long articleId) {
        commentRepository.deleteAllByArticle(getArticleOrElseThrow(articleId));
        articleRepository.deleteById(articleId);
    }

    public boolean isNotAuthor(long articleId, long userId) {
        Article article = getArticleOrElseThrow(articleId);
        return article.isNotMatchAuthor(userId);

    }

    public Article getArticleOrElseThrow(long articleId) {
        return articleRepository
                .findById(articleId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private User getUserOrElseThrow(long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<Comment> getComments(long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }
}
