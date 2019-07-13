package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    public static final int INITIAL_ID = 0;
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void save(final Article article) {
        if (articles.isEmpty()) {
            article.setId(INITIAL_ID);
            articles.add(article);
            return;
        }

        article.setId(getMaxId() + 1);
        articles.add(article);
    }

    private long getMaxId() {
        return articles.stream().mapToLong(article -> article.getId()).max().getAsLong();

    }

    public Optional<Article> findById(final Long id) {
        for (Article article : articles) {
            if (article.hasSameId(id)) {
                return Optional.of(article);
            }
        }
        return Optional.empty();
    }

    public void update(final Article article) {
        Long id = article.getId();
        articles.set(id.intValue(), article);
    }

    public void deleteById(final Long id) {
        //TODO OPTIONAL ERROR SHOULD BE HANDLED
        Article article = findById(id).get();
        articles.remove(article);
    }

    public void deleteAll() {
        articles.clear();
    }
}
