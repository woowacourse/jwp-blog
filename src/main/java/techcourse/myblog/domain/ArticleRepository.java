package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ArticleRepository {
    private static final String defaultBackgroundImageUrl = "images/pages/index/study.jpg";

    private static int nextArticleNumber = 0;

    private final List<Article> articles = new ArrayList<>();

    public void write(final Article article) {
        if (article.getCoverUrl().length() == 0) {
            article.setCoverUrl(defaultBackgroundImageUrl);
        }
        article.setNumber(++nextArticleNumber);
        articles.add(article);
    }

    public Optional<Article> find(int articleNumber) {
        final int index = Collections.binarySearch(
                articles.stream()
                        .map(Article::getNumber)
                        .collect(Collectors.toList()),
                articleNumber
        );
        return (index != -1) ? Optional.of(articles.get(index)) : Optional.empty();
    }

    public void edit(final Article article, final int index) {
        articles.set(index - 1, article);
    }

    public void delete(final int index) {
        articles.remove(index - 1);
    }

    public List<Article> findAll() {
        return this.articles;
    }
}