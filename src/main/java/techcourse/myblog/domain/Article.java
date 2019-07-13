package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Article {
    private static int currentId = 1;

    private final int id;
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this(currentId++, title, contents, coverUrl);
    }

    private Article(int id, String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.id = id;
    }

    public Article getCopy() {
        return new Article(id, title, contents, coverUrl);
    }

    public void update(Article article) {
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
    }
}
