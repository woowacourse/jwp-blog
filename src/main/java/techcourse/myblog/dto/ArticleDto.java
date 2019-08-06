package techcourse.myblog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import techcourse.myblog.domain.article.ArticleDetails;

@Data
@EqualsAndHashCode(of = "id")
public class ArticleDto {
    private Long id;
    private ArticleDetails articleDetails;
}
