package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public Article findArticleById(int articleId) {
        return articles.stream()
                .filter(article -> article.hasSameIdWith(articleId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));
    }

    public void editArticle(int id, String newTitle, String newCoverUrl, String newContents) {
        Article oldArticle = findArticleById(id);
        oldArticle.update(newTitle, newCoverUrl, newContents);
    }

    public void deleteArticle(int id) {
        Article article = findArticleById(id);
        articles.remove(article);
    }
}
