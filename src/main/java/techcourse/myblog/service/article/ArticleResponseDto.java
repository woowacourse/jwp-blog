package techcourse.myblog.service.article;

import techcourse.myblog.service.comment.CommentResponseDto;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class ArticleResponseDto {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String coverUrl;

    @NotNull
    private String contents;

    @NotNull
    private List<CommentResponseDto> comments;

    public ArticleResponseDto(Long id, @NotNull String title, @NotNull String coverUrl, @NotNull String contents, @NotNull List<CommentResponseDto> comments) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.comments = comments;
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

    public List<CommentResponseDto> getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleResponseDto that = (ArticleResponseDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
