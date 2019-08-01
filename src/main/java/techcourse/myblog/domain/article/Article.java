package techcourse.myblog.domain.article;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ArticleVo articleVo;

    @ManyToOne
    private User author;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    private Article() {
    }

    public Article(User author, ArticleVo articleVo) {
        this.author = author;
        this.articleVo = articleVo;
    }

    public void updateArticle(ArticleVo articleVo, Long userId) {
        if (!author.matchId(userId)) {
            throw new UserMismatchException();
        }
        this.articleVo = articleVo;

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
        return articleVo.getTitle();
    }

    public String getCoverUrl() {
        return articleVo.getCoverUrl();
    }

    public String getContents() {
        return articleVo.getContents();
    }
}
