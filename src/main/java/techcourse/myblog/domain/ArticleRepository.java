package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private final AtomicInteger id = new AtomicInteger();

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public void insert(Article article) {
        articles.add(article);
        id.getAndIncrement();
    }

    public Article findById(int articleId) {
        return articles.stream()
                .filter(article -> article.matchId(articleId))
                .findAny()
                .orElseThrow(() -> new ArticleNotFoundException("존재하지 않는 게시물입니다."))
                ;
    }

    public void update(Article article) {
        Article updateArticle = findById(article.getId());
        updateArticle.update(article);
    }

    public int nextId() {
        return id.get();
    }

    public void remove(int articleId) {
        Article removeArticle = findById(articleId);
        articles.remove(removeArticle);
    }
}
