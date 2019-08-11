package techcourse.myblog.domain.article;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ArticleUserInput articleUserInput;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    protected Article() {
    }

    public Article(User author, ArticleUserInput articleUserInput) {
        this.author = author;
        this.articleUserInput = articleUserInput;
    }

    public void updateArticle(ArticleUserInput articleVo, Long userId) {
        if (!author.matchId(userId)) {
            throw new UserMismatchException();
        }
        this.articleUserInput = articleVo;

    }

    public boolean matchUserId(Long userId) {
        return this.author.matchId(userId);
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return author.getId();
    }

    public String getTitle() {
        return articleUserInput.getTitle();
    }

    public String getCoverUrl() {
        return articleUserInput.getCoverUrl();
    }

    public String getContents() {
        return articleUserInput.getContents();
    }
}
