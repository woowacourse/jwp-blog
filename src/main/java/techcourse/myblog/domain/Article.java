package techcourse.myblog.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import techcourse.myblog.service.MismatchAuthorException;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    @CreationTimestamp
    private LocalDateTime createTimeAt;
    @UpdateTimestamp
    private LocalDateTime updateTimeAt;

    private Article() {
    }

    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public Article update(Article article) {
        checkAuth(article.getAuthor());
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();

        return this;
    }

    public void checkAuth(User user) {
        if (user == null || !user.equals(author)) {
            throw new MismatchAuthorException("작성자만 접근할 수 있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public User getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                ", contents='" + contents + '\'' +
                '}';
    }
}
