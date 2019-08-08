package techcourse.myblog.domain.article;

import techcourse.myblog.domain.BaseTimeEntity;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
public class Article extends BaseTimeEntity {
    private static final int CONTENTS_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false, length = CONTENTS_LENGTH)
    @Lob
    private String contents;

    @ManyToOne
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "FK_article_to_user"))
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Comment> comments;

    protected Article() {
    }

    public Article(String title, String coverUrl, String contents, User author) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public void updateArticle(Article article) {
        if (matchAuthor(article.author)) {
            this.title = article.title;
            this.coverUrl = article.coverUrl;
            this.contents = article.contents;
        }
    }

    public boolean matchAuthor(User author) {
        return this.author.equals(author);
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return author.getId();
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
}
