package techcourse.myblog.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class ArticleDto {
    public final static String TITLE_CONSTRAINT_MESSAGE = "제목을 입력해주세요.";
    public final static String CONTENTS_CONSTRAINT_MESSAGE = "내용을 입력해주세요.";

    @NotBlank(message = TITLE_CONSTRAINT_MESSAGE)
    private String title;

    @NotNull
    private String coverUrl;

    @NotBlank(message = CONTENTS_CONSTRAINT_MESSAGE)
    private String contents;

    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article toArticle(User user) {
        return new Article(title, coverUrl, contents, user);
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
