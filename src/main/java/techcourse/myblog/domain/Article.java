package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.service.exception.AuthException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @Column(nullable = false)
    private String coverUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_article_to_user"), nullable = false)
    private User author;

    @OneToMany(mappedBy = "article", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Article(final String title, final String contents, final String coverUrl, final User author) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void update(Article other) {
        this.title = other.title;
        this.coverUrl = other.coverUrl;
        this.contents = other.contents;
    }

    public boolean isWrittenBy(final Long other) {
        if (author.getId().equals(other)) {
            return true;
        }
        throw new AuthException("작성자가 아닙니다.");
    }
}


