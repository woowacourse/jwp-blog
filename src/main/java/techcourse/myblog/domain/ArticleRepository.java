package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {
    private AtomicLong nextId = new AtomicLong(1);
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article findById(long id) {
        return articles.stream()
                .filter(article -> article.matchId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public void add(Article article) {
        article.setId(nextId.getAndIncrement());
        articles.add(article);
    }

    public void deleteById(long id) {
        articles.removeIf(article -> article.matchId(id));
    }
}
