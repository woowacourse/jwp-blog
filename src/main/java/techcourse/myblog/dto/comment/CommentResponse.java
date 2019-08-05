package techcourse.myblog.dto.comment;

import org.springframework.data.annotation.CreatedDate;
import techcourse.myblog.domain.user.User;

import java.util.Date;

public class CommentResponse {
    private Long id;
    private String contents;
    private User commenter;
    @CreatedDate
    private Date date;

    public CommentResponse() {
    }

    public CommentResponse(Long id, String contents, User commenter) {
        this.id = id;
        this.contents = contents;
        this.commenter = commenter;
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

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }

    public Date getDate() {
        return date;
    }
}
