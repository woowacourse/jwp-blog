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

    public void addArticle(Article article) {
        articles.add(article);
    }

    public Article findArticleById(int articleId) {
        return articles.stream()
                .filter(article -> article.getId() == articleId)
                .findFirst()
                .get();
    }

    public void editArticle(int id, Article article) {
        Article oldArticle = findArticleById(id);
        oldArticle.update(article);
    }

    public void deleteArticle(int id) {
        Article article = findArticleById(id);
        articles.remove(article);
    }
}
