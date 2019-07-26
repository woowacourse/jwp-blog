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
    private Long id;
    private String title;
    private String coverUrl;
    @Lob
    private String contents;

    @Builder
    private Article(Long id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
}
