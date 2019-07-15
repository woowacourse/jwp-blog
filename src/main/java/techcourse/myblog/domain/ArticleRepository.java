package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleRepository {
    private static final long ADD_COUNT = 1;

    private List<Article> articles = new ArrayList<>();
    private long latestId = 0;

    public long add(ArticleDto articleDto) {
        Article article = articleDto.toArticle(latestId());
        articles.add(article);
        return latestId;
    }

    private synchronized long latestId() {
        latestId = latestId + ADD_COUNT;
        return latestId;
    }

    public ArticleDto findById(long expectId) {
        Article temp = articles.stream()
                .filter(article -> article.isSameId(expectId))
                .findFirst().orElseThrow(IllegalArgumentException::new);
        return temp.toDto();
    }

    public long updateById(ArticleDto expected, long updateId) {
        Article temp = articles.stream()
                .filter(article -> article.isSameId(updateId))
                .findFirst().orElseThrow(IllegalAccessError::new);

        temp.setTitle(expected.getTitle());
        temp.setCoverUrl(expected.getCoverUrl());
        temp.setContents(expected.getContents());
        return updateId;
    }

    public boolean deleteById(final long articleId) {
        Article temp = articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst().orElseThrow(IllegalAccessError::new);

        return articles.remove(temp);
    }

    public List<ArticleDto> findAll() {
        return articles.stream()
                .map(article -> article.toDto())
                .collect(Collectors.toList());
    }

    public void deleteAll() {
        latestId = 0;
        articles.clear();
    }
}
