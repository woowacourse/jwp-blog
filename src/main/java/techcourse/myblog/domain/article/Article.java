package techcourse.myblog.domain.article;

import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Article {
    private static final String DEFAULT_URL = "/images/default/bg.jpg";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String contents;

    private String coverUrl;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public Article(String title, String contents, String coverUrl, User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = getDefaultUrl(coverUrl);
        this.author = author;
    }

    protected Article() {

    }

    private String getDefaultUrl(String coverUrl) {
        if (coverUrl.isEmpty()) {
            return DEFAULT_URL;
        }
        return coverUrl;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void update(Article modifiedArticle) {
        this.title = modifiedArticle.title;
        this.contents = modifiedArticle.contents;
        this.coverUrl = modifiedArticle.coverUrl;
    }

    public boolean isAuthor(User author) {
        return this.author.isMatchEmail(author);
    }

    public boolean matchId(long id) {
        return this.id == id;
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

    public List<Comment> getComments() {
        return new ArrayList<>(this.comments);
    }

    public User getAuthor() {
        return author;
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