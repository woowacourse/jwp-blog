package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
                .ifPresent(a -> findArticleIndexByArticle(a)
                        .ifPresent(i -> articles.remove(i)));
    }

    public void updateArticle(Article updatedArticle) {
        int targetArticleIndex = findArticleIndexByArticle(updatedArticle).getAsInt();

        articles.remove(targetArticleIndex);
        articles.add(targetArticleIndex, updatedArticle);
    }

    private OptionalInt findArticleIndexByArticle(Article article) {
        return IntStream.range(0, articles.size())
                .filter(i -> articles.get(i).hasSameArticleId(article))
                .findFirst();
    }
}
