package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.web.ArticleNotFoundException;

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

    public Article findById(int articleId) {
        for (Article article : articles) {
            if (articleId == article.getId()) {
                return article;
            }
        }
        throw new ArticleNotFoundException();
    }

    public int getSize() {
        return articles.size();
    }

    public Article getLatestArticle() {
        return articles.get(articles.size() - 1);
    }
}
