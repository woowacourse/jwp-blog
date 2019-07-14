package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private static final int DEFAULT_ARTICLE_INDEX = 0;

    private List<Article> articles = new ArrayList<>();
    private Long nextArticleId = 1L;

    public Long addArticle(Article article) {
        Long latestId = generateNewId();

        article.setArticleId(latestId);
        this.articles.add(article);

        return latestId;
    }

    public Optional<Article> findArticleById(final Long articleId) {
        return articles.stream()
                .filter(x -> x.getArticleId().equals(articleId))
                .findAny();
    }

    public List<Article> findAll() {
        return articles;
    }

    public Long generateNewId() {
        return nextArticleId++;
    }

    public void deleteArticle(Long articleId) {
        findArticleById(articleId)
                .ifPresent(a -> articles.remove(findArticleIndexByArticle(a)));
    }

    public void updateArticle(Article updatedArticle) {
        int targetArticleIndex = findArticleIndexByArticle(updatedArticle);

        articles.remove(targetArticleIndex);
        articles.add(targetArticleIndex, updatedArticle);
    }

    private int findArticleIndexByArticle(Article article) {
        int targetArticleIndex = DEFAULT_ARTICLE_INDEX;

        for (int i = 0; i < articles.size(); i++) {
            targetArticleIndex = articles.get(i).getArticleId().equals(article.getArticleId()) ? i : targetArticleIndex;
        }

        return targetArticleIndex;
    }
}
