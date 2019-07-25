package techcourse.myblog.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class ArticleDto {
    @NotBlank(message = "제목을 작성해주세요.")
    private String title;

    @NotBlank(message = "내용을 작성해주세요.")
    private String contents;

    @NotBlank(message = "url을 작성해주세요.")
    private String coverUrl;
}
