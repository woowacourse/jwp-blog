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

    private int getMaxId() {
        return articles.stream().mapToInt(article -> article.getId()).max().getAsInt();

    }

    public Optional<Article> findById(final int id) {
        for (Article article : articles) {
            if (article.hasSameId(id)) {
                return Optional.of(article);
            }
        }
        return Optional.empty();
    }

    public void update(final Article article) {
        int id = article.getId();

        articles.set(id, article);
    }

    public void deleteById(final int id) {
        //TODO OPTIONAL ERROR SHOULD BE HANDLED
        Article article = findById(id).get();
        articles.remove(article);
    }

    public void deleteAll() {
        articles.clear();
    }
}
