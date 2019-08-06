package techcourse.myblog.domain.article;

import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Article {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(nullable = false)
    private User author;
    private String title;
    private String coverUrl;
    private String contents;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Article() {}

    public Article(User author, String title, String coverUrl, String contents) {
        this.author = Optional.ofNullable(author).orElseThrow(IllegalArgumentException::new);
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void update(Article toUpdate) {
        this.title = toUpdate.title;
        this.coverUrl = toUpdate.coverUrl;
        this.contents = toUpdate.contents;
    }

    public Long getId() {
        return this.id;
    }

    public User getAuthor() {
        return this.author;
    }

    public boolean isSameAuthor(Article article) {
        return this.author.equals(article.author);
    }

    public boolean isSameAuthor(User user) {
        return this.author.equals(user);
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