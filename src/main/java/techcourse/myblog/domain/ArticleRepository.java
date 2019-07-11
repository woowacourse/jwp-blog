package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void save(Article article){
        articles.add(article);
    }

    public Article findById(Long articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId().equals(articleId))
                .findAny()
                .get();
    }

    public int size() {
        return articles.size();
    }

    public void update(Article originArticle, Article updatedArticle) {
        articles.remove(originArticle);
        save(updatedArticle);
    }
}
