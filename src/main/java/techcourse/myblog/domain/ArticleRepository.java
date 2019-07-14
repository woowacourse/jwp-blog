package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private int index;

    public List<Article> findAll() {
        return articles;
    }

    public Article create(Article article) {
        articles.add(article);
        index++;
        return article;
    }

    public Article findById(final int articleId) {
        return articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Article findLatest() {
        return articles.get(index);
    }

    public int getLatestIndex(){
        return index;
    }

    public void delete(int articleId) {
        Article article = findById(articleId);
        articles.remove(article);
    }

    public void modify(final int articleId, final Article article) {
        delete(articleId);
        articles.add(article);
    }
}
