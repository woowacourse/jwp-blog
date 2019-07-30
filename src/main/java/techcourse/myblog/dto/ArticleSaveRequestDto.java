package techcourse.myblog.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ArticleSaveRequestDto {
    private String title;
    private String coverUrl;
    private String contents;
}
