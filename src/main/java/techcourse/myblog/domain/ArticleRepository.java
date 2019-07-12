package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();
    private Long index = 0L;

    public ArticleRepository() {
        articles.add(new Article(++index, "브라운 잘 생겼다.", "https://avatars3.githubusercontent.com/u/46308949?s=460&v=4", "존잘![image](https://files.slack.com/files-pri/TFELTJB7V-FL8V2PAR3/image_from_ios.jpg)"));
        articles.add(new Article(++index, "뚱이", "data:image/jpeg;base64,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", "변희봉 닮음"));
    }

    public List<Article> findAll() {
        return new ArrayList<>(articles);
    }

    public Optional<Long> save(final Article article) {
        Long id = ++index;
        article.setId(id);
        articles.add(article);
        return Optional.of(id);
    }

    public Optional<Article> findById(final Long id) {
        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst();
    }

    public Long update(final Article editedArticle) {
        Long id = editedArticle.getId();

        return articles.stream()
                .filter(article -> article.isSameId(id))
                .findFirst()
                .map(article -> articles.set(articles.indexOf(article), editedArticle))
                .orElseThrow(() -> new IllegalArgumentException("not found id=" + id))
                .getId();
    }

    public void deleteById(final Long id) {
        Article article = findById(id).orElseThrow(() -> new IllegalArgumentException("not found id=" + id));
        articles.remove(article);
    }
}
