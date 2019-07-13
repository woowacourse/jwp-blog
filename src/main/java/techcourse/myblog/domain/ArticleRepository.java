package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private long nextId = 1;
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void add(Article article) {
        article.setId(nextId++);
        articles.add(article);
    }

    public Article findById(long id) {
        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public void clear() {
        articles.clear();
    }

    public void deleteById(long id) {
        articles.removeIf(article -> article.getId() == id);
    }
}
