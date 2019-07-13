package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private static int lastId = 0;

    public List<Article> findAll() {
        List<Article> reverseArticles = new ArrayList<>(articles);
        Collections.reverse(reverseArticles);
        return Collections.unmodifiableList(reverseArticles);
    }

    public synchronized void saveArticle(Article article) {
        article.setId(++lastId);
        articles.add(article);
    }

    public Article getArticleById(int index) {
        return articles.stream()
                .filter(article -> article.isSameId(index))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void removeArticleById(int id) {
        Optional.of(getArticleById(id)).orElseThrow(IllegalArgumentException::new);
        articles.removeIf(article -> article.isSameId(id));
    }

    public void updateArticle(Article updatedArticle) {
        articles.set(articles.indexOf(getArticleById(updatedArticle.getId())), updatedArticle);
    }


}
