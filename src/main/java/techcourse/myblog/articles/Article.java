package techcourse.myblog.articles;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.articles.comments.Comment;
import techcourse.myblog.exception.AuthException;
import techcourse.myblog.users.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @Column(nullable = false)
    private String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_article_to_user"), nullable = false)
    private User author;

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(final String title, final String contents, final String coverUrl, final User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    void update(Article other) {
        this.title = other.title;
        this.coverUrl = other.coverUrl;
        this.contents = other.contents;
    }

    boolean isWrittenBy(final User other) {
        if (this.author.equals(other)) {
            return true;
        }
        throw new AuthException("작성자가 아닙니다.");
    }
}


