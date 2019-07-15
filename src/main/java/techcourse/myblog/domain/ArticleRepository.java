package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private int id = 0;

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public void save(Article article) {
        id = id + 1;
        article.setId(id);
        articles.add(article);
    }

    public Article findArticleById(int id) {
        return articles.stream()
                .filter(x -> x.checkId(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당하는 ID의 기사가 없습니다."));
    }

    public void removeById(int id) {
        articles.remove(findArticleById(id));
    }
}
