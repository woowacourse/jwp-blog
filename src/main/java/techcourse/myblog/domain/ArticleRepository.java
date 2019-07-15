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
    private AtomicInteger newArticleId = new AtomicInteger(1);

    private Map<Integer, Article> articles = new TreeMap<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    public int saveArticle(Article article) {
        int id = newArticleId.getAndIncrement();
        article.changeId(id);

        articles.put(id, article);
        return id;
    }

    public Article findById(int articleId) {
        checkExistArticleId(articleId);

        return articles.get(articleId);
    }

    public void modifyArticle(Article article) {
        int articleId = article.getId();

        checkExistArticleId(articleId);

        articles.put(articleId, article);
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
}
