package techcourse.myblog.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Article {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User author;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false, length = 50)
    private String contents;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Article(User author, String title, String coverUrl, String contents) {
        this.author = author;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public boolean isSameAuthor(Article article) {
        return this.author.equals(article.author);
    }

    public boolean isSameAuthor(User user) {
        return this.author.equals(user);
    }

    public void writeComment(Comment comment) {
        comments.add(comment);
    }
}