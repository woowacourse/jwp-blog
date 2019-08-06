package techcourse.myblog.comment.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200)
    private String contents;

    @ManyToOne
    private User author;

    @ManyToOne
    private Article article;

    @Builder
    private Comment(long id, String contents, User author, Article article) {
        this.id = id;
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public Comment updateComment(String contents) {
        this.contents = contents;
        return this;
    }

    public boolean notMatchAuthorId(long authorId) {
        return author.getId() != authorId;
    }
}
