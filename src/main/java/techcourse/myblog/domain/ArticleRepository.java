package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.exception.CouldNotFindArticleIdException;
import techcourse.myblog.web.ArticleDto;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final int INITIAL_ID = 0;
    private static final int INCREMENT_AMOUNT = 1;

    private List<Article> articles;
    private int lastGeneratedId;

    public ArticleRepository() {
        articles = new ArrayList<>();
        lastGeneratedId = INITIAL_ID;
    }

    public void save(ArticleDto articleDto) {
        int articleId = generateNewId();
        Article article = Article.of(articleId, articleDto);

        articles.add(article);
    }

    private int generateNewId() {
        int generatedId = lastGeneratedId + INCREMENT_AMOUNT;
        lastGeneratedId = generatedId;

        return generatedId;
    }

    public Article findBy(int articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId() == articleId)
                .findAny()
                .orElseThrow(CouldNotFindArticleIdException::new);
    }

    public List<Article> findAll() {
        return articles;
    }

    public void updateBy(int articleId, ArticleDto updatedArticleDto) {
        Article updatedArticle = Article.of(articleId, updatedArticleDto);

        Article oldArticle = findBy(articleId);
        int indexOfOld = articles.indexOf(oldArticle);

        articles.set(indexOfOld, updatedArticle);
    }

    public void deleteBy(int articleId) {
        Article article = findBy(articleId);
        articles.remove(article);
    }

    public int getLastGeneratedId() {
        return lastGeneratedId;
    }
}
