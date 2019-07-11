package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepository {
    private Articles articles = new Articles();
    private int articleId = 0;

    public Articles findAll() {
        return articles;
    }

    public void save(Article article) {
        article.setId(++articleId);
        articles.add(article);
    }

    public Article find(int articleId) {
        return articles.find(articleId);
    }

    public void saveEdited(Article editedArticle) {
        delete(editedArticle.getId());
        articles.add(editedArticle);
    }

    public void delete(int articleId) {
        articles.remove(articleId);
    }
}
