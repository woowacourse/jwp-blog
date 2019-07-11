package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public long save(Article article) {
        articles.add(article);
        return article.getId();
    }

    public Article find(long articleId) {
        return articles.stream()
                .filter(article -> article.equals(articleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("찾는 게시물이 없습니다."));
    }

    public long update(Article updatedArticle) {
        Article article = find(updatedArticle.getId());
        int atUpdateIndex = articles.indexOf(article);
        articles.set(atUpdateIndex, updatedArticle);

        return article.getId();
    }

    public void delete(long articleId) {
        Article article = find(articleId);
        articles.remove(article);
    }
}
