package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {
    private final AtomicLong nextId = new AtomicLong();
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void add(Article article) {
        article.setId(nextId.incrementAndGet());
        articles.add(article);
    }

    public Optional<Article> findById(long id) {
        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst();
    }

    public void clear() {
        articles.clear();
    }

    public void deleteById(long id) {
        articles.removeIf(article -> article.isSameId(id));
    }
}
