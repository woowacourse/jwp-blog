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

    public void add(Article article) {
        articles.add(article);
    }

    public Article get(int articleId) {
        return articles.get(articleId);
    }

    public int lastIndex() {
        return size() - 1;
    }

    public void update(int articleId, Article article) {
        articles.set(articleId, article);
    }

    public int size(){
        return articles.size();
    }

    public void remove(int articleId) {
        articles.remove(articles.get(articleId));
    }
}
