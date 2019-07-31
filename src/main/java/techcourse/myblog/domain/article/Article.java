package techcourse.myblog.domain.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Article {
    private static final String DEFAULT_URL = "/images/default/bg.jpg";
    private static final Logger log = LoggerFactory.getLogger(Article.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 40, nullable = false)
    private String title;

    @Lob
    private String contents;

    @Column(name = "coverUrl")
    private String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = getDefaultUrl(coverUrl);
    }

    protected Article() {
    }

    private String getDefaultUrl(String coverUrl) {
        if (coverUrl.isEmpty()) {
            return DEFAULT_URL;
        }
        return coverUrl;
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

    public Long getId() {
        return id;
    }

    public void update(Article modifiedArticle) {
        log.debug("article Ready to Save {}", modifiedArticle.getId());
        this.title = modifiedArticle.title;
        this.contents = modifiedArticle.contents;
        this.coverUrl = modifiedArticle.coverUrl;
        log.debug("article Save done {}", this.id);
    }

    public boolean matchId(long id) {
        return this.id == id;
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
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