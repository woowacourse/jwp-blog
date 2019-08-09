package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Comment;

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

}
