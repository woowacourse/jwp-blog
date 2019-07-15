package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.ArticleDto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private AtomicInteger id;

    public ArticleRepository() {
        id = new AtomicInteger(articles.size());
    }

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article save(ArticleDto articleDto) {
        Article article = articleDto.toArticle(id.getAndIncrement());
        articles.add(article);
        return article;
    }

    public Article findById(int articleId) {
        return articles.stream()
                .filter(article -> article.matchId(articleId))
                .findAny()
                .orElseThrow(() -> new ArticleNotFoundException("존재하지 않는 게시물입니다."))
                ;
    }

    public void modify(Article editedArticle) {
        Article article = findById(editedArticle.getId());
        article.update(editedArticle);
    }

    public void remove(int articleId) {
        Article removeArticle = findById(articleId);
        articles.remove(removeArticle);
    }
}
