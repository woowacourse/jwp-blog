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

    public void save(Article article) {
        articles.add(article);
    }

    public Article findById(int articleId) {
        if (articleId < 0 || articleId >= articles.size()) {
            return null;
        }
        return articles.get(articleId);
    }

    public int getSize() {
        return articles.size();
    }
}
