package techcourse.myblog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import techcourse.myblog.domain.Article;

public class ArticleDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotNull
    private String coverUrl;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    public ArticleDto(String title, String url, String contents) {
        this.title = title;
        this.coverUrl = url;
        this.contents = contents;
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


    public Article toArticle() {
        return new Article(title, coverUrl, contents);
    }
}
