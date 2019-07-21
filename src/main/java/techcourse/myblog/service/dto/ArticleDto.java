package techcourse.myblog.service.dto;

import lombok.Getter;
import techcourse.myblog.domain.Article;

import javax.validation.constraints.NotBlank;

@Getter
public class ArticleDto {
    @NotBlank(message = "제목은 빈 칸일 수 없습니다.")
    private String title;
    @NotBlank(message = "배경 Url은 빈 칸일 수 없습니다.")
    private String coverUrl;
    @NotBlank(message = "내용은 빈 칸일 수 없습니다.")
    private String contents;
    
    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article toArticle() {
        return Article.from(title, coverUrl, contents);
    }
}
