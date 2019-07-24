package techcourse.myblog.web.dto;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;

public class ArticleRequestDto {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @URL(message = "배경 이미지는 URL 형식이어야 합니다")
    private String coverUrl;
    @NotBlank(message = "내용을 입력해주세요")
    private String contents;

    public static ArticleRequestDto of(String title, String coverUrl, String contents) {
        ArticleRequestDto dto = new ArticleRequestDto();
        dto.title = title;
        dto.coverUrl = coverUrl;
        dto.contents = contents;
        return dto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
