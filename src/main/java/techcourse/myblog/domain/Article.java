package techcourse.myblog.domain;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import techcourse.myblog.dto.ArticleSaveParams;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String coverUrl;
    private String contents;

    @Builder
    public Article(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public boolean isCoverUrl() {
        return StringUtils.isNotBlank(coverUrl);
    }

    public void update(ArticleSaveParams articleSaveParams) {
        this.title = articleSaveParams.getTitle();
        this.coverUrl = articleSaveParams.getCoverUrl();
        this.contents = articleSaveParams.getContents();
    }
}
