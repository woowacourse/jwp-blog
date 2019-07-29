package techcourse.myblog.domain;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    public Comment() {
    }

    public Comment(String contents, User author, Article article) {
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public User getAuthor() {
        return author;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public String toString() {
        return "Comment{" + contents + "}";
    }
}
