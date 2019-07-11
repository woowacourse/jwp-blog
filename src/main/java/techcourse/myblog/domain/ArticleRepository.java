package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.exception.NotFoundArticleException;

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
                .orElseThrow(NotFoundArticleException::new);
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
