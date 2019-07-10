package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.dto.ArticleDto;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public long save(ArticleDto.Create articleDto) {
        Article article = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());
        articles.add(article);
        return article.getId();
    }

    public Article find(int index) {
        return articles.get(index);
    }
}
