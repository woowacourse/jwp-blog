package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;
import techcourse.myblog.domain.exception.IllegalIdException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    public static final int INITIAL_ID = 0;
    private List<Article> articles = new ArrayList<>();

    public List<Article> findAll() {
        return articles;
    }

    public void save(final Article article) {
        article.setId(createId());
        articles.add(article);
    }

    private int createId() {
        if (articles.isEmpty()) {
            return INITIAL_ID;
        }
        return getMaxId() + 1;
    }

    private int getMaxId() {
        return articles.stream().mapToInt(article -> article.getId()).max().getAsInt();

    }

    public Optional<Article> findById(final int id) {
        Optional<Article> optional = articles.stream()
                .filter(article -> article.hasSameId(id)).findAny();

        if (optional.isEmpty()) {
            throw new IllegalIdException("입력받은 id를 조회할 수 없습니다.");
        }

        return optional;
    }

    public void update(final Article newArticle) {
        int id = newArticle.getId();
        Article originalArticle = findById(id).get();

        articles.set(articles.indexOf(originalArticle), newArticle);
    }

    public void deleteById(final int id) {
        //TODO OPTIONAL ERROR SHOULD BE HANDLED
        Article article = findById(id).get();
        articles.remove(article);
    }

    public void deleteAll() {
        articles.clear();
    }
}
