package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private Article article;

    public List<Article> findAll() {
        return articles;
    }

    public void save(final Article article) {
        if (articles.isEmpty()) {
            article.setId(0);
            articles.add(article);
            return;
        }
        article.setId(articles.get(articles.size() - 1).getId() + 1);
        articles.add(article);
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
