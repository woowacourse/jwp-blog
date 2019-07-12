package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final int FIRST_ID = 0;
    private static final int ID_INCREASE_RANGE = 1;
    private List<Article> articles = new ArrayList<>();
    private int lastId = FIRST_ID;

    private ArticleRepository() {
    }

    public void add(Article article) {
        articles.add(article);
    }

    public Article getArticleById(int index) {
        return articles.stream()
                .filter(article -> article.getId() == index)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public void removeArticleById(int id) {
        int originalArticleSize = articles.size();
        articles.removeIf(a -> a.getId() == id);
        if (originalArticleSize == articles.size()) {
            throw new IllegalArgumentException();
        }
    }

    public void updateArticleById(Article updatedArticle, int id) {
        Article originalArticle = getArticleById(id);
        originalArticle.updateTitle(updatedArticle);
        originalArticle.updateUrl(updatedArticle);
        originalArticle.updateContents(updatedArticle);
    }

    public void save(Article article) {
        this.lastId += ID_INCREASE_RANGE;
        article.setId(this.lastId);
        this.add(article);
    }

    public int getLastId() {
        return lastId;
    }

    public void setLastId(int lastId) {
        this.lastId = lastId;
    }

    public List<Article> findAll() {
        return articles;
    }

    public void deleteAll(){
        articles.clear();
    }

}
