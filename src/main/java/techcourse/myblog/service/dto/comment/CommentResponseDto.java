package techcourse.myblog.service.dto.comment;

import java.util.Objects;

public class CommentResponseDto {
    private Long id;
    private String content;
    private Long authorId;
    private String authorName;

    public CommentResponseDto(Long id, String content, Long authorId, String authorName) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentResponseDto that = (CommentResponseDto) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
