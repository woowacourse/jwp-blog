package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.web.exception.NotExistEntityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ArticleRepository {
    private static final int AUTO_INCREMENT_START_ID = 1;
    private static final int MIN_ARTICLE_ID = 1;

    private AtomicInteger newArticleId = new AtomicInteger(AUTO_INCREMENT_START_ID);

    private Map<Integer, Article> articles = new TreeMap<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    public int saveArticle(Article article) {
        int id = article.getId();

        if (isNewArticle(id)) {
            id = newArticleId.getAndIncrement();
            article.setId(id);
        }

        articles.put(id, article);
        return id;
    }

    public Article findById(int articleId) {
        checkExistArticleId(articleId);

        return articles.get(articleId);
    }

    public void removeArticle(int articleId) {
        checkExistArticleId(articleId);

        articles.remove(articleId);
    }

    private void checkExistArticleId(int articleId) {
        if (!articles.containsKey(articleId)) {
            throw new NotExistEntityException();
        }
    }

    private boolean isNewArticle(int id) {
        return id < MIN_ARTICLE_ID;
    }
}
