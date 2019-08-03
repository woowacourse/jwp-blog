package techcourse.myblog.domain;

import org.springframework.lang.NonNull;
import techcourse.myblog.exception.UnauthenticatedUserException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Article {
    public Article() {
    }

    public Article(String title, String contents, String coverUrl, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    @Id
    @Column(name = "ARTICLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(length = 100)
    private String title;

    @NonNull
    @Column(length = 1000)
    private String contents;

    @NonNull
    @Column(length = 100)
    private String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @NonNull
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public User getAuthor() {
        return author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void update(Article article) {
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
    }

    public void add(Comment comment) {
        comments.add(comment);
    }

    public void remove(Comment comment) {
        comments.remove(comment);
    }

    public void validate(User user) {
        if (author.equals(user) == false) {
            throw new UnauthenticatedUserException();
        }
    }
}
