package techcourse.myblog.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@EqualsAndHashCode
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    private User author;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Article() {
    }

    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    public Article(String title, String coverUrl, String contents, User author) {
        this(title, coverUrl, contents);
        this.author = author;
    }

    public Article update(Article article) {
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();
        return this;
    }

    public boolean add(Comment comment) {
        try {
            comments.add(comment);
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    public boolean remove(Comment comment) {
        try {
            comments.remove(comment);
            return true;
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }

    public boolean isAuthorized(User user) {
        return this.author.equals(user);
    }
}
