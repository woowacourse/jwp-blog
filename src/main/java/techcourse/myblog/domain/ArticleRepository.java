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

    public int insertArticle(Article article) {
        articles.add(article);
        return articles.size() - 1;
    }

    public Article findById(int articleId) {
        return articles.get(articleId);
    }
}
