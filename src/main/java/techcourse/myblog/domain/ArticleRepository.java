package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ArticleRepository {
    private static final long ADD_COUNT = 1;

    private List<Article> articles = new ArrayList<>();
    private long latestId = 0;

    public long add(final Article article) {
        article.setId(latestId());
        articles.add(article);
        return latestId;
    }

    private synchronized long latestId() {
        latestId = latestId + ADD_COUNT;
        return latestId;
    }

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article findById(final long articleId) {
        return articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public long updateById(final Article articleParam, final long articleId) {
        Article article = findById(articleId);
        article.setArticle(articleParam);
        return articleId;
    }

    public boolean deleteById(final long articleId) {
        Article article = findById(articleId);
        return articles.remove(article);
    }

    public void deleteAll() {
        latestId = 0;
        articles = new ArrayList<>();
    }
}
