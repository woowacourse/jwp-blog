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
        article.setArticleId(++articleId);
        articles.add(article);
    }

    public Article find(long articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId() == articleId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                ;
    }

    public void saveEdited(Article editedArticle) {
        Article articleToDelete = articles.stream()
                .filter(article -> article.getArticleId() == editedArticle.getArticleId())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        articles.remove(articleToDelete);
        articles.add(editedArticle);
    }
}
