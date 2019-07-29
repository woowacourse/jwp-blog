package techcourse.myblog.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.article.Article;
import techcourse.myblog.user.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @ManyToOne
    @JoinColumn(name = "article", foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    @Column(nullable = false)
    private String contents;

    @Builder
    public Comment(User author, Article article, String contents) {
        this.author = author;
        this.article = article;
        this.contents = contents;
    }
}
