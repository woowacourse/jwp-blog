package techcourse.myblog.dto;

import javax.validation.constraints.NotBlank;

public class ArticleDto {
    private static final String BLANK_TITLE = "제목을 입력해주세요.";
    private static final String BLANK_CONTENTS = "내용을 입력해주세요.";

    @NotBlank(message = BLANK_TITLE)
    private String title;

    private String coverUrl;

    @NotBlank(message = BLANK_CONTENTS)
    private String contents;

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
