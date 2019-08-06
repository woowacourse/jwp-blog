package techcourse.myblog.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

@Getter
public class ArticleDto implements DomainDto<Article> {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotNull
    private String coverUrl;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    public ArticleDto(String title, String coverUrl, String contents) {
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    @Override
    public Article toDomain() {
        return new Article(title, coverUrl, contents);
    }

    public Article toDomain(User author) {
        return new Article(title, coverUrl, contents, author);
    }
}
