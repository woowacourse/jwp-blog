package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.excerption.ArticleNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class ArticleRepository {
    public static final int INIT_ARTICLE_ID = 1;

    private List<Article> articles = new ArrayList<>();
    private int latestId = INIT_ARTICLE_ID;

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article findById(final int id) {
        return articles.stream()
                .filter(article -> article.match(id))
                .findFirst()
                .orElseThrow(ArticleNotFoundException::new);
    }

    public Article save(final Article article) {
        checkNull(article);
        articles.add(article);
        latestId++;
        return articles.get(articles.size() - 1);
    }

    public void update(final int id, final Article article) {
        checkNull(article);
        Article oldArticle = findById(id);
        oldArticle.update(article);
    }

    private void checkNull(Article article) {
        if (Objects.isNull(article)) {
            throw new NullPointerException();
        }
    }

    public void delete(final int id) {
        Article article = findById(id);
        articles.remove(article);
    }

    public int getLatedId() {
        return latestId;
    }
}
