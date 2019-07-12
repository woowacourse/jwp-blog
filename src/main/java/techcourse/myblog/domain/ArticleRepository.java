package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private long index = 0L;
    public List<Article> findAll() {
        return articles;
    }

    public void add(Article article) {
        article.setId(index++);
        articles.add(article);
    }

    public Optional<Article> findById(final long articleId) {
        return articles.stream()
                .filter(article-> article.isSameId(articleId))
                .findFirst();
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
