package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.exception.CouldNotFindArticleIdException;
import techcourse.myblog.web.ArticleDTO;

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

    public void save(ArticleDTO articleDTO) {
        int articleId = generateNewId();
        String title = articleDTO.getTitle();
        String coverUrl = articleDTO.getCoverUrl();
        String contents = articleDTO.getContents();

        Article article = new Article(articleId, title, coverUrl, contents);
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

    public void updateBy(int updatintId, ArticleDTO articleDTO) {
        Article updatedArticle = Article.of(updatintId, articleDTO);
        Article oldArticle = findBy(updatintId);
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

    public List<Article> findAll() {
        return articles;
    }
}
