package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // target entity의 column 이름이 name
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
    private String title;
    private String coverUrl;
    @Lob
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

    public boolean matchAuthorId(Long userId) {
        return this.author.matchId(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
