package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private static final int NEXT_ID = 1;
    private static final int DEFAULT_ARTICLE_INDEX = 0;

    private List<Article> articles = new ArrayList<>();

    public void addArticle(Article article) {
        this.articles.add(article);
    }

    public Article findArticleById2(final long articleId) {
        Article target = null;

        for (Article article : articles) {
            target = article.matchId(articleId) ? article : target;
        }

        return target;
    }

    public List<Article> findAll() {
        return articles;
    }

    public void deleteArticle(Long articleId) {
        articles.remove(findArticleIndexByArticle(findArticleById2(articleId)));
    }

    public void updateArticle(Article updatedArticle) {
        Article original = findArticleById2(updatedArticle.getArticleId());
        original.update(updatedArticle);
    }

    private int findArticleIndexByArticle(Article article) {
        int targetArticleIndex = DEFAULT_ARTICLE_INDEX;

        for (int i = 0; i < articles.size(); i++) {
            targetArticleIndex = articles.get(i).getArticleId() == article.getArticleId() ? i : targetArticleIndex;
        }

        return targetArticleIndex;
    }
}
