package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class Article {
    private static int currentId = 1;

    private int id;
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    private Article(int id, String title, String contents, String coverUrl) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public Article copyArticles() {
        return new Article(id, title, contents, coverUrl);
    }

    public boolean isEqualId(int articleId) {
        return articleId == id;
    }

    public int getCurrentId() {
        return currentId++;
    }

    public static void initCurrentId() {
        currentId = 1;
    }

    public void editArticle(Article article) {
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.coverUrl = article.getCoverUrl();
    }
}
