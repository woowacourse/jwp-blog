package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleRepository {

    private static Long articleId = 0L;
    private List<Article> articles = new ArrayList<>();

    public int size() {
        return articles.size();
    }

    public List<Article> findAll() {
        return articles;
    }

    public void save(Article article) {
        article.setId(++articleId);
        articles.add(article);
    }

    public Article findById(Long id) {
        return articles.stream()
                .filter(article -> article.getId().equals(id))
                .findAny()
                .get();
    }

    public void update(Article editedArticle) {
        for (int i = 0; i < this.size(); i++) {
            if (articles.get(i).isSameId(editedArticle)) {
//                Article originArticle=articles.get(i);
//                originArticle.setTitle(editedArticle.getTitle());
//                originArticle.setCoverUrl(editedArticle.getCoverUrl());
//                originArticle.setContents(editedArticle.getContents());
                articles.set(i,editedArticle);
            }
        }
    }
}
