package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private int index;

    public List<Article> findAll() {
        return articles;
    }

    public void create(Article article) {
        article.setId(index++);
        articles.add(article);
    }

    public Article findById(final int articleId) {
        return articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Article findLastest() {
        return articles.get(lastestIndex());
    }

    public int lastestIndex() {
        return articles.size() - 1;
    }

    public void delete(int articleId) {
        Article article = findById(articleId);
        articles.remove(article);
    }

    public void modify(final int articleId, final Article article) {
        delete(articleId);
        articles.add(article);
    }
}
