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

    public boolean save(Article article) {
        article.setId(++articleId);
        articles.add(article);
        return true;
    }

    public Article findById(Long id) {
        return articles.stream()
                .filter(article -> article.getId().equals(id))
                .findAny()
                .get();
    }

    public boolean update(Article editedArticle) {
//        for (int i = 0; i < this.size(); i++) {
//            if (articles.get(i).isSameId(editedArticle)) {
//                articles.set(i, editedArticle);
//                return true;
//            }
//        }
//        return false;
//        위가 좋은지 아래가 좋은지 궁금해요 json :D

        articles.stream()
                .filter(editedArticle::isSameId)
                .findFirst()
                .ifPresentOrElse(article -> {
                    article.setTitle(editedArticle.getTitle());
                    article.setCoverUrl(editedArticle.getCoverUrl());
                    article.setContents(editedArticle.getContents());
                },()-> new IllegalArgumentException("오류"));
        return true;
    }

    public boolean deleteById(Long articleId) {
        return articles.removeIf(article -> article.getId().equals(articleId));
    }
}
