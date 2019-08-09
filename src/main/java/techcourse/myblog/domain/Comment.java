package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.NotMatchCommentAuthorException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    private String contents;

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

    public static class CommentBuilder {
        private String contents;
        private User author;
        private Article article;

        public CommentBuilder contents(String contents) {
            this.contents = contents;
            return this;
        }

        public CommentBuilder author(User author) {
            this.author = author;
            return this;
        }

        public CommentBuilder article(Article article) {
            this.article = article;
            return this;
        }

        public Comment build() {
            return new Comment(this);
        }
    }

    public Comment(CommentBuilder builder) {
        this.contents = builder.contents;
        this.author = builder.author;
        this.article = builder.article;
    }

    public Comment(String contents, User author, Article article) {
        this(null, contents, author, article);
    }

    public Comment(Long id, String contents, User author, Article article) {
        this.id = id;
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public boolean isNotAuthor(User user) {
        return !author.equals(user);
    }

    public void modify(CommentDto commentDto, User user) {
        if (isNotAuthor(user)) {
            throw new NotMatchCommentAuthorException("댓글의 작성자가 아닙니다!");
        }
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
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Comment comment = (Comment) object;
        return id == comment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" + contents + "}";
    }
}