package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public void addArticle(final Article article) {
        articles.add(article);
    }

    public Article findArticleById(final int articleId) {
        return articles.stream()
                .filter(article -> article.getId() == articleId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public void editArticle(final int id, final Article article) {
        Article oldArticle = findArticleById(id);
        oldArticle.update(article);
    }

    public void deleteArticle(final int id) {
        Article article = findArticleById(id);
        articles.remove(article);
    }
}
