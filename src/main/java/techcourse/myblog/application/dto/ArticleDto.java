package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.support.validator.TitleConstraint;

public class ArticleDto {
    private Long id;
    @TitleConstraint
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleDto(Long id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public Article toEntity(User author) {
        return new Article(author, title, coverUrl, contents);
    }
}
