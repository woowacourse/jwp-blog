package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
    private String title;
    private String coverUrl;
    private String contents;

    private Article() {
    }

    public Article(User author, String title, String coverUrl, String contents) {
        this.author = author;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void updateArticle(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public boolean isSameAuthor(User author) {
        return this.author.equals(author);
    }

    public Long getId() {
        return id;
    }

    public User getAuthor() {
        return author;
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
}
