package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.web.exception.NotExistEntityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {
    private static final Long AUTO_INCREMENT_START_ID = 1L;

    private AtomicLong newArticleId = new AtomicLong(AUTO_INCREMENT_START_ID);

    private Map<Long, Article> articles = new TreeMap<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    public Long saveArticle(Article article) {
        Long id = newArticleId.getAndIncrement();
        article.setId(id);

        articles.put(id, article);
        return id;
    }

    public Article findById(Long articleId) {
        checkExistArticleId(articleId);

        return articles.get(articleId);
    }

    public void removeArticle(Long articleId) {
        checkExistArticleId(articleId);

        articles.remove(articleId);
    }

    private void checkExistArticleId(Long articleId) {
        if (!articles.containsKey(articleId)) {
            throw new NotExistEntityException();
        }
    }
}
