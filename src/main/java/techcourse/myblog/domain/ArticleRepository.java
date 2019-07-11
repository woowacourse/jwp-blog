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

    public void add(Article article) {
        articles.add(article);
    }

    public Article findById(int id) {
        return articles.stream()
                .filter(x -> x.getId() == id)
                .findFirst()
                .get();
    }

    public int issuedId() {
        int size = articles.size();
        if (size == 0) {
            return 0;
        }
        return articles.get(size - 1).getId() + 1;
    }

    public void updateArticle(int id, Article article) {
        int index = 0;
        for (int i = 0; i < articles.size(); i++) {
            if (articles.get(i).getId() == id) {
                index = i;
            }
        }

        articles.set(index, article);
    }

    public void deleteById(int id) {
        articles.remove(findById(id));
    }
}
