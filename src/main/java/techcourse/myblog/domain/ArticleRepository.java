package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public void save(Article article) {
        articles.add(article);
    }

    public Article findById(Long articleId) {
        System.out.println(articles.toString());

        return articles.stream()
                .filter(article -> article.isEqualToArticleId(articleId))
                .findAny().orElseThrow(() -> new IllegalArgumentException("해당 인스턴스가 없습니다."));
    }

    public int count() {
        return articles.size();
    }

    public void update(Long articleId, ArticleVO articleVO) {
        Article article = findById(articleId);
        article.update(articleVO);
    }

    public void delete(Long articleId) {
        Article article = findById(articleId);
        articles.remove(article);
    }
}
