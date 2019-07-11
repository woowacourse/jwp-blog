package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {
    private List<Article> articles = new ArrayList<>();

    public ArticleRepository() {
        articles.add(new Article("aaaaa", "https://previews.123rf.com/images/romeolu/romeolu1601/romeolu160100122/50594417-%EB%88%88-%EB%B0%B0%EA%B2%BD.jpg", "ccccc"));
        articles.add(new Article("title222", "https://previews.123rf.com/images/romeolu/romeolu1601/romeolu160100122/50594417-%EB%88%88-%EB%B0%B0%EA%B2%BD.jpg", "content2222"));
        articles.add(new Article("333333", "https://previews.123rf.com/images/romeolu/romeolu1601/romeolu160100122/50594417-%EB%88%88-%EB%B0%B0%EA%B2%BD.jpg", "3333"));
        articles.get(0).setId(0);
        articles.get(1).setId(1);
        articles.get(2).setId(2);
    }

    // TODO: 2019-07-10 고치기....
    private int lastId = 1;

    public List<Article> findAll() {
        return articles;
    }

    public void add(Article article) {
        articles.add(article);
    }

    public int getLastId() {
        return lastId;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public Article getArticleById(int index) {
        return articles.stream()
                .filter(article -> article.getId() == index)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
