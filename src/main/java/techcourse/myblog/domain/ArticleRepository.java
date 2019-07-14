package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.dto.ArticleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public Article add(ArticleDto articleDto) {
        Article newArticle = new Article(articleDto);
        articles.add(newArticle);
        return newArticle;
    }

    public void add(Article article) {
        articles.add(article);
    }

    public Optional<Article> findById(long id) {
        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst();
    }

    public void clear() {
        articles.clear();
    }

    public void deleteById(long id) {
        articles.removeIf(article -> article.isSameId(id));
    }
}
