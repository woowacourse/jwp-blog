package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Comment;

import java.time.LocalDateTime;

public class CommentResponse extends BaseResponse {

    private CommentDto comment;

    public CommentResponse() {
        super(" ");
    }

    public CommentResponse(Comment comment) {
        super("ok");
        this.comment = new CommentDto(comment.getId(),
            comment.getContents(),
            comment.getAuthor().getName(),
            comment.getCreatedTime()
        );
    }

    public CommentDto getComment() {
        return comment;
    }

    public void setComment(CommentDto comment) {
        this.comment = comment;
    }

    public static class CommentDto {
        private Long id;
        private String contents;
        private String authorName;
        private LocalDateTime createdTime;

        public CommentDto() {
        }

        public CommentDto(Long id, String contents, String authorName, LocalDateTime createdTime) {
            this.id = id;
            this.contents = contents;
            this.authorName = authorName;
            this.createdTime = createdTime;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

        public LocalDateTime getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(LocalDateTime createdTime) {
            this.createdTime = createdTime;
        }
    }
}
