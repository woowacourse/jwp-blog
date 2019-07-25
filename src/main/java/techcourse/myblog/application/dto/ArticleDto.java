package techcourse.myblog.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {
    @NotBlank
    private String title;

    @NotBlank
    private String coverUrl;

    @NotBlank
    private String contents;
}
