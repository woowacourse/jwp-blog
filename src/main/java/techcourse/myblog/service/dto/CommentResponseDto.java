package techcourse.myblog.service.dto;

public class CommentResponseDto {
    private Long id;
    private Long authorId;
    private String authorName;
    private String comment;

    public CommentResponseDto() {
    }

    public CommentResponseDto(Long id, Long authorId, String authorName, String comment) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getComment() {
        return comment;
    }
}
