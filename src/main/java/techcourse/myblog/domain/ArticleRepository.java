package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private int id;

    public List<Article> findAll() {
        return articles;
    }

    public void insert(Article article) {
        articles.add(article);
        id++;
    }

    public Article find(int articleId) {
        return articles.stream()
                .filter(article -> article.matchId(articleId))
                .findAny()
                .orElseThrow(() -> new InvalidArticleException("존재하지 않는 게시물입니다."))
                ;
    }

    public void update(Article article) {
        int index = articles.indexOf(find(article.getId()));
        articles.set(index, article);
    }

    public int nextId() {
        return id;
    }

    public void remove(int articleId) {
        Article removeArticle = find(articleId);
        articles.remove(removeArticle);
    }
}
