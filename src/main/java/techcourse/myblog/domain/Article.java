package techcourse.myblog.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;
    private String title;
    private String coverUrl;
    private String contents;

    @Builder
    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void update(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.contents = articleDto.getContents();
        this.coverUrl = articleDto.getCoverUrl();

    }

}
