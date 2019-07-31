package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Article() {
    }

    public Article(String title, String coverUrl, String contents, User user) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.user = user;
    }

    public void modify(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
    }

    public void checkAuthor(String email) {
        if (user.isDifferentEmail(email)) {
            throw new IllegalArgumentException();
        }
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