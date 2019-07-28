package techcourse.myblog.articles;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import techcourse.myblog.users.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Lob
    private String contents;

    @Column(nullable = false)
    private String coverUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_article_to_user"))
    private User author;

    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime regDate;

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    @Builder
    private Article(final String title, final String contents, final String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
    }

    void update(Article other) {
        this.updateDate = LocalDateTime.now();
        this.title = other.title;
        this.coverUrl = other.coverUrl;
        this.contents = other.contents;
    }

    boolean isWrittenBy(final User user) {
        return this.author.equals(user);
    }
}


