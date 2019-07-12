package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private static final int NEXT_ID = 1;
    private static final int DEFAULT_ARTICLE_INDEX = 0;

    private List<Article> articles = new ArrayList<>();

    public void addArticle(Article article) {
        this.articles.add(article);
    }

    public Optional<Article> findArticleById(final Long articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId().equals(articleId))
                .findAny();
    }

    public List<Article> findAll() {
        return articles;
    }

    public Long generateNewId() {
        long maxId = 0L;

        for (Article article : articles) {
            maxId = Math.max(article.getArticleId(), maxId);
        }

        return maxId + NEXT_ID;
    }

    public void deleteArticle(Long articleId) {
        findArticleById(articleId)
                .ifPresent(article -> articles.remove(findArticleIndexByArticle(article)));
    }

    public void updateArticle(Article updatedArticle) {
        int targetArticleIndex = findArticleIndexByArticle(updatedArticle);
        articles.set(targetArticleIndex, updatedArticle);
    }

    private int findArticleIndexByArticle(Article article) {
        int targetArticleIndex = DEFAULT_ARTICLE_INDEX;

        for (int i = 0; i < articles.size(); i++) {
            targetArticleIndex = articles.get(i).getArticleId().equals(article.getArticleId()) ? i : targetArticleIndex;
        }

        return targetArticleIndex;
    }
}
