package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private final List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public void addBlog(Article article) {
        articles.add(article);
    }

    public Article find(int i) {
        return articles.get(i);
    }

    public void delete(int i) {
        articles.remove(i);
    }

    public void replace(int id, Article article) {
        articles.set(id, article);
    }
}