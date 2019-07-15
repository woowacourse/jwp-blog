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

    public void save(Article article) {
        articles.add(article);
    }

    public Article findById(long articleId) {
        return articles.stream()
                .filter(article -> article.isEqualTo(articleId))
                .findFirst()
                .orElseThrow(NotFoundArticleException::new);
    }

    public void update(Article updatedArticle) {
        Article article = findById(updatedArticle.getId());
        article.updateArticle(updatedArticle);
    }

    public void delete(long articleId) {
        Article article = findById(articleId);
        articles.remove(article);
    }
}
