package techcourse.myblog.service.dto.comment;

import java.util.Objects;

public class CommentResponseDto {
    private String content;
    private String authorName;

    public CommentResponseDto(String content, String authorName) {
        this.content = content;
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorName() {
        return authorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentResponseDto that = (CommentResponseDto) o;
        return Objects.equals(content, that.content) &&
                Objects.equals(authorName, that.authorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, authorName);
    }
}
