package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private int articleId = 0;

    public List<Article> findAll() {
        return articles;
    }

    public void save(Article article) {
        article.setId(++articleId);
        articles.add(article);
    }

    public Article find(long articleId) {
        return articles.stream()
                .filter(article -> article.getId() == (int) articleId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                ;
    }

    public void saveEdited(Article editedArticle) {
        delete(editedArticle.getId());
        articles.add(editedArticle);
    }

    public void delete(long articleId) {
        Article articleToDelete = articles.stream()
                .filter(article -> article.getId() == (int) articleId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        articles.remove(articleToDelete);
    }
}
