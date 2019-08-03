package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.vo.CommentContents;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private String contents;
    @Embedded
    private CommentContents commentContents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    private Comment() {
    }

    public Comment(CommentContents commentContents, User author, Article article) {
//        this.contents = contents;
        this.commentContents = commentContents;
        this.author = author;
        this.article = article;
    }

    public static List<CommentDto> toDto(List<Comment> comments) {
        return comments.stream()
                .map(comment -> comment.toDto())
                .collect(Collectors.toList());
    }

    private CommentDto toDto() {
        return new CommentDto(id, commentContents.getContents(), author, article);
    }

    public Long getId() {
        return id;
    }

    public CommentContents getContents() {
        return commentContents;
    }

    public User getAuthor() {
        return author;
    }

    public Article getArticle() {
        return article;
    }

    @Override
    public String toString() {
        return "Comment{" + commentContents.getContents() + "}";
    }

    public void changeContent(CommentContents commentContents) {
//        contents = commentDto.getContents();
        this.commentContents = commentContents;
    }

//    public void changeContent(CommentDto commentDto) {
////        contents = commentDto.getContents();
//        commentContents = this.commentContents;
//    }

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
