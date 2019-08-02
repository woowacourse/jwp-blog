package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // target entity의 column 이름이 name
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;
    private String title;
    private String coverUrl;
    private String contents;

    private Article() {
    }

    public Article(User author, String title, String coverUrl, String contents) {
        this.author = author;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void updateArticle(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public boolean isSameAuthor(User author) {
        return this.author.equals(author);
    }

    // todo : 점진적 리팩토링. User or userId 무엇으로 비교하는 게 좋을까.
    public boolean isSameAuthor2(Long userId) {
        return author.getId().equals(userId);
    }
}
