package techcourse.myblog.service.dto.article;

import techcourse.myblog.service.dto.comment.CommentResponseDto;

import javax.validation.constraints.NotNull;
import java.util.List;

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
}
