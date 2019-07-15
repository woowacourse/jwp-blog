package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.dto.ArticleUpdateDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article findById(long id) {
        return articles.stream()
                .filter(article -> article.matchId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public Long save(Article article) {
        articles.add(article);
        return article.getId();
    }

    public void updateById(long id, ArticleUpdateDto articleUpdateDto) {
        findById(id).updateArticle(
                articleUpdateDto.getTitle(),
                articleUpdateDto.getCoverUrl(),
                articleUpdateDto.getContents());

    }

    public void deleteById(long id) {
        articles.removeIf(article -> article.matchId(id));
    }
}
