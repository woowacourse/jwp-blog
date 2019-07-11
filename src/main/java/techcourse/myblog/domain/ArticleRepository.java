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

    public int getSize() {
        return articles.size();
    }

    public Article getArticleById(int articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId() == articleId)
                .findFirst()
                .get();
    }
}
