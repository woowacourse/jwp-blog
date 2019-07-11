package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private long latestId = 0;

    public List<Article> findAll() {
        return articles;
    }

    public long add(final Article article) {
        article.setId(latestId());
        articles.add(article);
        return latestId;
    }

    private long latestId() {
        latestId = latestId + 1;
        return latestId;
    }

    public Article findById(final long latestId) {
        return articles.stream()
                .filter(article -> article.getId() == latestId)
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public long updateById(Article articleParam, long articleId) {
        Article article = findById(articleId);
        article.setTitle(articleParam.getTitle());
        article.setCoverUrl(articleParam.getCoverUrl());
        article.setContents(articleParam.getContents());
        return articleId;
    }

    public boolean deleteById(final long articleIdParam) {
        Article article = findById(articleIdParam);
        return articles.remove(article);
    }
}
