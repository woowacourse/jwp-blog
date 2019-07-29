package techcourse.myblog.service.dto.article;

import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.user.UserRequestDto;
import techcourse.myblog.service.dto.user.UserResponseDto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ArticleDto {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String coverUrl;

    @NotNull
    private String contents;

    public ArticleDto(final String title, final String coverUrl, final String contents) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(coverUrl);
        Objects.requireNonNull(contents);

        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleDto that = (ArticleDto) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(coverUrl, that.coverUrl) &&
                Objects.equals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, coverUrl, contents);
    }
}
