package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleRepository {
    private static final long INITIAL_ID = 0;
    private static final long ADD_COUNT = 1;

    private List<Article> articles = new ArrayList<>();
    private long latestId = 0;

    public long add(final ArticleDto articleDto) {
        Article article = articleDto.toArticle(latestId());
        articles.add(article);
        return latestId;
    }

    private synchronized long latestId() {
        latestId = latestId + ADD_COUNT;
        return latestId;
    }

    public ArticleDto findById(final long id) {
        Article article = findArticleById(id);
        return article.toDto();
    }

    public long updateById(final ArticleDto articleDto, final long id) {
        Article article = findArticleById(id);
        article.update(articleDto.toArticle(id));
        return id;
    }

    public boolean deleteById(final long id) {
        Article article = findArticleById(id);
        return articles.remove(article);
    }

    private Article findArticleById(final long id) {
        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public List<ArticleDto> findAll() {
        return articles.stream()
                .map(Article::toDto)
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        latestId = INITIAL_ID;
        articles.clear();
    }
}
