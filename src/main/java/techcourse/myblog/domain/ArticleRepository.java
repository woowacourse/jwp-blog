package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.dto.ArticleDto;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public void addArticle(ArticleDto dto) {
        articles.add(new Article(dto));
    }

    public Article findByIndex(int index) {
        return articles.get(index);
    }

    public void deleteByIndex(int index) {
        articles.remove(index);
    }

    public int articleCount() {
        return articles.size();
    }
}
