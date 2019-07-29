package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.domain.User;

@Getter
@Setter
public class ArticleDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    private User author;
}
