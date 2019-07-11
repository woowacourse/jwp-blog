package techcourse.myblog.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Articles implements Iterable<Article> {
    private List<Article> articles;

    public Articles() {
        this.articles = new ArrayList<>();
    }

    public Articles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public Iterator<Article> iterator() {
        return articles.iterator();
    }

    public void add(Article article) {
        articles.add(article);
    }

    public Article find(int articleId) {
        return articles.stream()
                .filter(article -> article.getId() == articleId)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                ;
    }

    public void remove(int articleId) {
        Article articleToDelete = find(articleId);
        articles.remove(articleToDelete);
    }
}
