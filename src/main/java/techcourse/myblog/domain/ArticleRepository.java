package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class ArticleRepository {
    private Map<Integer, Article> articles = new TreeMap<>();
    private int newId = 0;

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    public int insertArticle(Article article) {
        articles.put(newId, article);
        return newId++;
    }

    public Article findById(int articleId) {
        return articles.get(articleId);
    }

    public void updateArticle(int articleId, Article article) {
        articles.put(articleId, article);
    }

    public void deleteArticle(int articleId) {
        articles.remove(articleId);
    }
}
