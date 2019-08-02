package techcourse.myblog.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import techcourse.myblog.application.service.exception.NotExistUserIdException;

import javax.persistence.*;

@Getter
@EqualsAndHashCode(of = "id")
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String coverUrl;

    @Lob
    @Column(nullable = false)
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

    public void modify(Article article, User user) {
        if (this.user.equals(user)) {
            this.title = article.title;
            this.coverUrl = article.coverUrl;
            this.contents = article.contents;
        }

        throw new NotExistUserIdException("작성자가 아닙니다.");
    }

    public boolean checkAuthor(String email) {
        return user.checkEmail(email);
    }
}