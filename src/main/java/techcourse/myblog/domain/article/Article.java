package techcourse.myblog.domain.article;

import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.exception.ArticleToUpdateNotFoundException;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;


@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false, length = 2000)
    private String contents;

    @ManyToOne()
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

    public void update(final Article article) {
        if (Objects.isNull(article)) {
            throw new ArticleToUpdateNotFoundException("업데이트 해야할 게시글이 없습니다.");
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
        return comments;
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
