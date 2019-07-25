package techcourse.myblog.repository;

import org.springframework.stereotype.Repository;
import techcourse.myblog.model.Article;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }
}
