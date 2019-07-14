package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {
    private static final long ID_INCREASE_RANGE = 1;
    private static final long START_ID = 0;
    private List<Article> articles = new ArrayList<>();
    private final AtomicLong lastId = new AtomicLong(START_ID);

    private ArticleRepository() {

    }

    public void add(Article article) {
        articles.add(article);
    }

    public void save(Article article) {
        lastId.set(lastId.get() + ID_INCREASE_RANGE);
        article.setId(lastId.get());
        this.add(article);
    }

    public void deleteAll() {
        articles.clear();
    }

    public Optional<Article> getArticleById(long index) {
        Optional<Article> maybeArticle = articles.stream()
                .filter(article -> article.getId() == index)
                .findFirst();

        return maybeArticle;
    }

    public void updateArticleById(Article updatedArticle, long id) {
        Optional<Article> maybeOriginalArticle = getArticleById(id);
        maybeOriginalArticle.ifPresent(a -> {
            a.updateTitle(updatedArticle);
            a.updateUrl(updatedArticle);
            a.updateContents(updatedArticle);
        });
    }

    public void removeArticleById(long id) {
        Optional<Article> maybeArticle = getArticleById(id);
        maybeArticle.ifPresent( a -> articles.removeIf(b -> b.getId() == id));
        maybeArticle.orElseThrow(IllegalArgumentException::new);
        articles.removeIf(a -> a.getId() == id);
    }

    public List<Article> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(articles));
    }
}
