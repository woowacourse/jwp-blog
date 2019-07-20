package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {

    private static Long articleId = 0L;
    private List<Article> articles = new ArrayList<>();

    public int size() {
        return articles.size();
    }

    public List<Article> findAll() {
        return articles;
    }

    public boolean save(Article article) {
        article.setId(++articleId);
        articles.add(article);
        return true;
    }

    public Article findById(Long id) {
        return articles.stream()
                .filter(article -> article.getId().equals(id))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    public boolean removeById(Long articleId) {
        return articles.removeIf(article -> article.getId().equals(articleId));
    }
}
