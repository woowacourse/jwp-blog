package techcourse.myblog.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private User author;
    private String title;
    private String coverUrl;
    private String contents;
    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    public Article() {}

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getAuthor() { return this.author; }

    public String getTitle() {
        return this.title;
    }

    public String getCoverUrl() {
        return this.coverUrl;
    }

    public String getContents() {
        return this.contents;
    }

    public void writeComment(Comment comment) {
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Article)) {
            return false;
        }
        Article rhs = (Article) o;
        return Objects.equals(this.title, rhs.title) &&
                Objects.equals(this.coverUrl, rhs.coverUrl) &&
                Objects.equals(this.contents, rhs.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.coverUrl, this.contents);
    }
}
