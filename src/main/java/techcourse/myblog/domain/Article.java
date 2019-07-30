package techcourse.myblog.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ARTICLE_ID")
    private Long id;
    private String title;
    private String coverUrl;
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_ARTICLE_USER"), nullable = false)
    private User author;

    @Builder
    private Article(Long id, String title, String coverUrl, String contents, User author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }
}
