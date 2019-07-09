package techcourse.myblog.domain;

import lombok.Data;

@Data
public class Article {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
}
