package techcourse.myblog.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Getter
@EqualsAndHashCode
public class Comment extends AbstractDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    public Comment() {
    }

    public Comment(final String contents) {
        this.contents = contents;
    }

    public Comment(String contents, User author, Article article) {
        this.contents = contents;
        this.author = author;
        this.article = article;
    }

    public Comment update(String contents) {
        this.contents = contents;
        return this;
    }

    @Override
    public boolean isAuthorized(User user) {
        return this.author.equals(user);
    }
}
