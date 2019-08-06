package techcourse.myblog.domain.article;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.date.BaseEntity;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Article extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @Column(nullable = false, length = 20)
    private String title;

    @Column(length = 100)
    private String coverUrl;

    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_to_comments"))
    @OrderBy("id ASC")
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public void update(Article article) {
        checkCorrespondingAuthor(article.getAuthor());
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
    }

    public void checkCorrespondingAuthor(User user) {
        if (!this.author.equals(user)) {
            throw new InvalidAuthorException();
        }
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public int getCountOfComment() {
        return comments.size();
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                ", author=" + author +
                ", comments=" + comments +
                '}';
    }
}
