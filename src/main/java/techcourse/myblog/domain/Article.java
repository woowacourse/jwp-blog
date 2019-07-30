package techcourse.myblog.domain;

import javax.persistence.*;

@Entity
public class Article {
    private static final String EMPTY_TEXT = "NULL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    private String contents;

    private Article() {
    }

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public Article update(Article article) {
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();

        return this;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
