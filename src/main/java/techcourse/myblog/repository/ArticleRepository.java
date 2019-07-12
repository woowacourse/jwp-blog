package techcourse.myblog.repository;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import techcourse.myblog.domain.Article;

@Repository
public class ArticleRepository {
    private static int articleCount = 0;
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article save(Article article) {
        article.setId(++articleCount);
        articles.add(article);
        return findById(articleCount);
    }

    public Article findById(int id) {
        return articles.stream()
                .filter(article -> article.matchId(id))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                ;
    }

    public void update(Article modifiedArticle) {
        findArticle(modifiedArticle).update(modifiedArticle);
    }

    private Article findArticle(Article article) {
        return findById(article.getId());
    }

    public void deleteById(int articleId) {
        articles.remove(findById(articleId));
    }
}
