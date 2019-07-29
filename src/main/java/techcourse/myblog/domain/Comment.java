package techcourse.myblog.domain;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Column(nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "article_id")
    @Column(nullable = false)
    private Article article;

    private Comment() {
    }

    public Comment(String contents, User author, Article article) {
        this.contents = contents;
        this.author = author;
        this.article = article;
    }
}
