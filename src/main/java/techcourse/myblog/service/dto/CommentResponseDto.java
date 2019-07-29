package techcourse.myblog.service.dto;

public class CommentResponseDto {
    private Long id;
    private String authorName;
    private String comment;

    public CommentResponseDto(Long id, String authorName, String comment) {
        this.id = id;
        this.authorName = authorName;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getComment() {
        return comment;
    }
}
