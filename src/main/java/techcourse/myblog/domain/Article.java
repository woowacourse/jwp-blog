package techcourse.myblog.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Article {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
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
}