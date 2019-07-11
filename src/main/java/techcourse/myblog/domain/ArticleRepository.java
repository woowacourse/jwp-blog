package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void insert(Article article) {
        articles.add(article);
    }

    public Article find(Integer articleId) {
        return articles.get(articleId);
    }

    public void update(Integer articleId, Article article) {
        articles.set(articleId, article);
    }

    public int size() {
        return articles.size();
    }
}
