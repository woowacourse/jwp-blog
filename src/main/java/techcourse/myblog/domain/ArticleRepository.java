package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.excerption.ArticleNotFoundException;
import techcourse.myblog.excerption.ArticleToSaveNotFoundException;
import techcourse.myblog.excerption.ArticleToUpdateNotFoundException;
import techcourse.myblog.excerption.InvalidArticleIdException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class ArticleRepository {
    public static final int INIT_ARTICLE_ID = 1;

    private List<Article> articles = new ArrayList<>();
    private int latestId = INIT_ARTICLE_ID;

    public List<Article> findAll() {
        return Collections.unmodifiableList(articles);
    }

    public Article findById(final int id) {
        if (id < INIT_ARTICLE_ID) {
            throw new InvalidArticleIdException("적절한 ID가 아닙니다.");
        }
        return articles.stream()
                .filter(article -> article.match(id))
                .findFirst()
                .orElseThrow(ArticleNotFoundException::new);
    }

    public Article save(final Article article) {
        if (Objects.isNull(article)) {
            throw new ArticleToSaveNotFoundException("저장할 게시글이 없습니다.");
        }
        articles.add(article);
        latestId++;
        return articles.get(articles.size() - 1);
    }

    public void update(final int id, final Article article) {
        if (id < INIT_ARTICLE_ID) {
            throw new InvalidArticleIdException("적절한 ID가 아닙니다.");
        }
        if (Objects.isNull(article)) {
            throw new ArticleToUpdateNotFoundException("업데이트 해야할 게시글이 없습니다.");
        }
        Article oldArticle = findById(id);
        oldArticle.update(article);
    }

    public void delete(final int id) {
        if (id < INIT_ARTICLE_ID) {
            throw new InvalidArticleIdException("적절한 ID가 아닙니다.");
        }
        Article article = findById(id);
        articles.remove(article);
    }

    public int getLatestId() {
        return latestId;
    }
}
