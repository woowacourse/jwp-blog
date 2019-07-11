package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private final int INITIAL_ID = 1;

    private List<Article> articles;
    private int lastArticleId;

    public ArticleRepository() {
        this.articles = new ArrayList<>();
        this.lastArticleId = INITIAL_ID;
    }

    public void save(Article article) {
        if (isSame(article)) {
            throw new IllegalArgumentException("중복된 ID가 있습니다.");
        }
        lastArticleId++;
        articles.add(article);
    }

    private boolean isSame(Article article) {
        return articles.contains(article);
    }

    public Article find(int articleId) {
        return articles.stream()
                .filter(article -> article.getArticleId() == articleId)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("ID를 찾을 수 없습니다."));
    }

    public void delete(int articleId) {
        Article article = find(articleId);

        articles.remove(article);
    }

    public void updateTitle(int articleId, String updatedTitle) {
        find(articleId).setTitle(updatedTitle);
    }

    public void updateCoverUrl(int articleId, String updateCoverUrl) {
        find(articleId).setCoverUrl(updateCoverUrl);
    }

    public void updateContents(int articleId, String updateContents) {
        find(articleId).setContents(updateContents);
    }

    public int getLastArticleId() {
        return lastArticleId;
    }

    public List<Article> findAll() {
        return articles;
    }
}
