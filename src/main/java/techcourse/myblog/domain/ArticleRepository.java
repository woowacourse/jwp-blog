package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class ArticleRepository {
    private Map<Integer, Article> articles = new TreeMap<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles.values());
    }

    public int insertArticle(Article article) {
        article = Article.from(article);
        int id = article.getId();
        articles.put(id, article);
        return id;
    }

    public Article findById(int articleId) {
        return articles.get(articleId);
    }

    public void updateArticle(Article article) {
        articles.put(article.getId(), article);
    }

    public void deleteArticle(int articleId) {
        articles.remove(articleId);
    }
}
