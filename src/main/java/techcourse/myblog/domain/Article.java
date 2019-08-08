package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
public class Article extends AbstractDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Column(nullable = false)
    @Lob
    private String contents;

    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

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
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
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

    @Override
    public boolean isAuthorized(User user) {
        return this.author.equals(user);
    }
}
