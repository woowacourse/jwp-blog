package techcourse.myblog.domain;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import techcourse.myblog.dto.ArticleDto;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Article {
    //TODO:ondelete
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String coverUrl;

    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User author;

    public boolean isWrittenBy(User user) {
        return author.equals(user);
    }

    public void update(ArticleDto.Update article) {
        title = article.getTitle();
        contents = article.getContents();
        coverUrl = article.getCoverUrl();
    }
}