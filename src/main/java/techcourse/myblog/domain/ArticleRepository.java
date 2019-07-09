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

    public int save(Article article) {
        articles.add(article);
        return articles.indexOf(article);
    }

    public Article find(int index) {
        return articles.get(index);
    }
}
