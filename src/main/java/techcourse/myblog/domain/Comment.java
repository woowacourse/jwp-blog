package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.application.dto.CommentDto;

import javax.persistence.*;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    public Comment() {
    }

    public Comment(String contents) {
        this.contents = contents;
    }

    public void init(User author, Article article) {
        this.author = author;
        this.article = article;
    }

    public void changeContent(CommentDto commentDto) {
        contents = commentDto.getContents();
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
