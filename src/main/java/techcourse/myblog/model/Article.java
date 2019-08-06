package techcourse.myblog.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.exception.MisMatchAuthorException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private Long id;

    @NonNull
    @Column(name = "title", nullable = false, length = 30)
    private String title;

    @NonNull
    @Column(name = "cover_url")
    private String coverUrl;

    @NonNull
    @Column(name = "contents", nullable = false, columnDefinition = "TEXT")
    @Lob
    private String contents;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime currentDateTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateTime;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User user;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getArticle() != this) {
            Comment commentWithArticleUpdated = new Comment(
                    comment.getContents(),
                    comment.getAuthor(),
                    this
            );
            comment.update(commentWithArticleUpdated);
        }
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public List<Comment> getSortedComments() {
        comments.sort(Comparator.comparing(Comment::getId));
        return Collections.unmodifiableList(comments);
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
    }

    public Article update(Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
        this.user = article.user;

        return this;
    }

    public void checkOwner(User user) {
        if (!this.user.equals(user)) {
            throw new MisMatchAuthorException("게시글을 작성한 유저만 수정할 수 있습니다.");
        }
    }
}
