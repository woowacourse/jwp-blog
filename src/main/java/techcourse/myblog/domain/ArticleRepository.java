package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final int NEXT_ID = 1;

    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public Long generateNewId() {
        long maxId = 0L;

        for (Article article : articles) {
            maxId = Math.max(article.getArticleId(), maxId);
        }
        return maxId + NEXT_ID;
    }
}
