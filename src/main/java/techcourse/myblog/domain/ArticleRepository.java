package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import org.springframework.web.server.ServerErrorException;

import java.rmi.ServerError;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private static final String defaultBackgroundImageUrl = "images/pages/index/study.jpg";

    private static int articleId = 0;

    private final List<Article> articles = new ArrayList<>();

    public void write(final Article article) {
        if (article.getCoverUrl().length() == 0) {
            article.setCoverUrl(defaultBackgroundImageUrl);
        }
        article.setId(++articleId);
        this.articles.add(article);
    }

    public int findIndexOfArticleById(final int articleId) {
        int index = Math.min(articleId, this.articles.size()) - 1;
        for (; index >= 0; index--) {
            if (this.articles.get(index).getId() == articleId) {
                return index;
            }
        }
        throw new IllegalArgumentException("none articleId server error");
    }

    public Optional<Article> find(final int articleId) {
        final int index = findIndexOfArticleById(articleId);
        return Optional.of(this.articles.get(index));
    }

    public List<Article> findAll() {
        return this.articles;
    }

    public void edit(final Article edited, final int articleId) {
        find(articleId).ifPresent(x -> x.setTo(edited));
    }

    public void delete(final int articleId) {
        final int index = findIndexOfArticleById(articleId);
        if (index != -1) {
            articles.remove(index);
        }
    }

    public void deleteAll(){
        articles.clear();
    }

    public int getArticleId() {
        return articleId;
    }
}