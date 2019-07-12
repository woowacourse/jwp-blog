package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.validator.CouldNotFindArticleIdException;
import techcourse.myblog.domain.validator.DuplicateArticleIdException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final int INITIAL_ID = 0;

    private List<Article> articles;
    private int lastAddedId;

    public ArticleRepository() {
        this.articles = new ArrayList<>();
        this.lastAddedId = INITIAL_ID;
    }

    public void save(Article article) {
        containsDuplicate(article);

        articles.add(article);
        lastAddedId = article.getArticleId();
    }

    private void containsDuplicate(Article article) {
        if (articles.contains(article)) {
            throw new DuplicateArticleIdException();
        }
    }

    public Article find(int articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId() == articleId)
                .findAny()
                .orElseThrow(CouldNotFindArticleIdException::new);
    }

    public void delete(int articleId) {
        Article article = find(articleId);
        articles.remove(article);
    }

    public void updateTitle(int articleId, String updatedTitle) {
        Article article = find(articleId);
        article.setTitle(updatedTitle);
    }

    public void updateCoverUrl(int articleId, String updateCoverUrl) {
        Article article = find(articleId);
        article.setCoverUrl(updateCoverUrl);
    }

    public void updateContents(int articleId, String updateContents) {
        Article article = find(articleId);
        article.setContents(updateContents);
    }

    public int getLastArticleId() {
        return lastAddedId;
    }

    public List<Article> findAll() {
        return articles;
    }
}
