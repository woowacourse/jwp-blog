package techcourse.myblog.comment.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class CommentResponse {
    private Long id;
    private String content;
    private Long authorId;
    private String authorName;
    private Long articleId;
    private String createdDate;

    public CommentResponse(Long id, String content, Long authorId, String authorName, Long articleId, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.articleId = articleId;
        if (!Objects.isNull(createdDate)) {
            this.createdDate = createdDate.toLocalDate() + " " + createdDate.toLocalTime();
        }
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

    public Long getArticleId() {
        return articleId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentResponse that = (CommentResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
