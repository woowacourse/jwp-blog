package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public Long addArticle(Article newArticle) {
        articles.add(newArticle);
        return newArticle.getId();
    }

    public Optional<Article> findById(Long id) {
        return articles.stream()
            .filter(a -> a.getId().equals(id))
            .findFirst();
    }

    public void deleteById(Long id) {
        Article toDelete = findById(id).orElseThrow(() -> new IllegalArgumentException("Article not found " + id));
        articles.remove(toDelete);
    }
}
