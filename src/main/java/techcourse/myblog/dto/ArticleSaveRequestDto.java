package techcourse.myblog.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ArticleSaveRequestDto {
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleSaveRequestDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
}
