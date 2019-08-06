package techcourse.myblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Article {
    private int id;
    private String title;
    private String contents;
    private String coverUrl;
}
