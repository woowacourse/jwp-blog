package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.web.dto.ArticleDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private static final AtomicLong lastId = new AtomicLong();

    public List<Article> findAll() {
        List<Article> reverseArticles = new ArrayList<>(articles);
        Collections.reverse(reverseArticles);
        return Collections.unmodifiableList(reverseArticles);
    }

    public long saveArticle(ArticleDto articleDto) {
        long id = lastId.incrementAndGet();
        articles.add(new Article(id, articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents()));
        return id;
    }

    public Optional<Article> getArticleById(long id) {
        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst();
    }

    public void removeArticleById(long id) {
        articles.remove(getArticleById(id).orElseThrow(IllegalArgumentException::new));
    }

    public void updateArticle(long id, Article updatedArticle) {
        articles.set(articles.indexOf(getArticleById(id)
                .orElseThrow(IllegalArgumentException::new)), updatedArticle);
    }
}
