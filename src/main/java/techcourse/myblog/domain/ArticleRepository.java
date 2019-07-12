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

    public Article findById(final int articleId) {
        return articles.get(articleId);
    }

    public Article findLastest() {
        return articles.get(lastestIndex());
    }

    public int lastestIndex() {
        return articles.size() - 1;
    }

    public void delete(final int articleId) {
        articles.remove(articleId);
    }

    public void modify(final Article article, final int articleId) {
        articles.set(articleId, article);
    }
}
