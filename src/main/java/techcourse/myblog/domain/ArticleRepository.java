package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    // TODO: 2019-07-10 고치기....
    private int lastId = 1;

    public List<Article> findAll() {
        return articles;
    }

    public void add(Article article) {
        articles.add(article);
    }

    public int getLastId() {
        return lastId;
    }
}
