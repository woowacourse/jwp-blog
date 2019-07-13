package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.web.ArticleNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        List<Article> copiedArticles = new ArrayList<>();

        for (Article article : articles) {
            copiedArticles.add(article.getCopy());
        }
        return copiedArticles;
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

    public void update(int articleId, Article article) {
        Article originalArticle = findById(articleId);

        originalArticle.update(article);
    }

    public void delete(int articleId) {
        for (Article article : articles) {
            if (articleId == article.getId()) {
                articles.remove(article);
                return;
            }
        }
        throw new ArticleNotFoundException();
    }

    public int getLatestArticleId() {
        int maxId = 0;
        for (Article article : articles) {
            if (article.getId() > maxId) {
                maxId = article.getId();
            }
        }
        return maxId;
    }
}