package techcourse.myblog.article.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleUpdateDto {
    private String title;
    private String coverUrl;
    private String contents;
}
