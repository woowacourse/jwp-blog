package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.excerption.ArticleNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article findArticleById(final int id) {
        return articles.stream()
                .filter(article -> article.match(id))
                .findFirst()
                .orElseThrow(ArticleNotFoundException::new);
    }

    public int addArticle(final Article article) {
        checkNull(article);
        articles.add(article);
        return article.getId();
    }

    public void updateArticle(final int id, final Article article) {
        checkNull(article);
        Article oldArticle = findArticleById(id);
        oldArticle.update(article);
    }

    private void checkNull(Article article) {
        if (Objects.isNull(article)) {
            throw new NullPointerException();
        }
    }

    public void deleteArticle(final int id) {
        Article article = findArticleById(id);
        articles.remove(article);
    }
}
