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

    public void save(final Article article) {
        article.setId(articles.size());
        articles.add(article);
    }

    public Article findById(final Long id) {
        return articles.get(id.intValue());
    }
}
