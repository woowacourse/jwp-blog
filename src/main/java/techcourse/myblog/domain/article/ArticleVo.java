package techcourse.myblog.domain.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ArticleVo {

    @Column(nullable = false)
    private String title;

    private String coverUrl;

    @Lob
    private String contents;
}
