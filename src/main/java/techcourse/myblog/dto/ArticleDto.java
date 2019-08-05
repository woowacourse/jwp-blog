package techcourse.myblog.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import techcourse.myblog.domain.article.ArticleDetails;

public class ArticleDto {
    @Data
    @EqualsAndHashCode(of = "id")
    public static class Response {
        private Long id;
        private ArticleDetails articleDetails;
    }
}
