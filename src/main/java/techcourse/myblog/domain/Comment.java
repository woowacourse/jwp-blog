package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.application.dto.CommentDto;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //todo: Long으로 바꾸기
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

    //todo:public =>private
    private Comment() {
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

    public void changeContent(CommentDto commentDto) {
        contents = commentDto.getContents();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
