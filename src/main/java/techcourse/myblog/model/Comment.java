package techcourse.myblog.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@RequiredArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_user"))
    private User author;

    @NonNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ARTICLE_ID", nullable = false, foreignKey = @ForeignKey(name = "fk_comment_to_article"))
    private Article article;

    @Column(name = "CONTENTS", nullable = false, columnDefinition = "TEXT")
    @NonNull
    private String contents;

    public Comment update(Comment comment) {
        this.author = comment.getAuthor();
        this.contents = comment.getContents();
        this.article = comment.getArticle();

        return this;
    }

    public void setArticle(Article article) {
        this.article = article;
        if(!article.getComments().contains(this)) {
            article.getComments().add(this);
        }
    }
}
