package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private int index;

    public List<Article> findAll() {
        return articles;
    }

    public Article create(Article article) {
        articles.add(article);
        index++;
        return article;
    }

    public Article findById(final int articleId) {
        return articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst()
                .orElseThrow(() -> new NotFoundArticleIdException("해당 아이디의 게시물을 찾을 수 없습니다."));
    }

    public int getLatestIndex() {
        return index;
    }

    public void delete(int articleId) {
        Article article = findById(articleId);
        articles.remove(article);
    }

    public void update(final int articleId, final Article updatedArticle) {
        articles.stream()
                .filter(article -> article.isSameId(articleId))
                .findFirst()
                .map(article -> articles.set(articles.indexOf(article), updatedArticle))
                .orElseThrow(() -> new NotFoundArticleIdException("해당 아이디의 게시물을 찾을 수 없습니다."));
    }
}
