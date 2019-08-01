package techcourse.myblog.domain.article;

import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleToUpdateNotFoundException;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.exception.UserNotLogInException;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String coverUrl;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;


    public Article() {
    }

    public Article(final String title, final String coverUrl, final String contents, User author) {
        this.title = Objects.requireNonNull(title);
        this.coverUrl = Objects.requireNonNull(coverUrl);
        this.contents = Objects.requireNonNull(contents);
        this.author = Objects.requireNonNull(author);
    }

    public void update(final Article article, final String email) {
        if (Objects.isNull(article)) {
            throw new ArticleToUpdateNotFoundException("업데이트 해야할 게시글이 없습니다.");
        }
        if (Objects.isNull(email)) {
            throw new UserNotLogInException();
        }
        if (!author.match(email)) {
            throw new UserHasNotAuthorityException();
        }
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
        this.author = article.getAuthor();
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

    public List<Comment> getComments() {
        if (Objects.isNull(comments)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(comments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(id, article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
