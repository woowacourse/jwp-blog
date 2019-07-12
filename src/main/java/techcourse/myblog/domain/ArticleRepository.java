package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ArticleRepository {
    private final List<Article> articles = new ArrayList<>();

    public List<ArticleDto> findAll() {
        return articles.stream()
                .map(Article::createDto)
                .collect(Collectors.toList());
    }

    public void addBlog(Article article) {
        articles.add(article);
    }

    public ArticleDto find(int i) {
        return articles.get(i).createDto();
    }

    public void delete(int i) {
        articles.remove(i);
    }

    public void replace(int id, Article article) {
        articles.set(id, article);
    }
}