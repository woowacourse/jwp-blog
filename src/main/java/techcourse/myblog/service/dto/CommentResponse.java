package techcourse.myblog.service.dto;

import techcourse.myblog.domain.User;

public class CommentResponse {
    private Long id;
    private String contents;
    private long elapsedTime;
    private User commenter;

    public CommentResponse(Long id, String contents, long elapsedTime, User commenter) {
        this.id = id;
        this.contents = contents;
        this.elapsedTime = elapsedTime;
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

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public User getCommenter() {
        return commenter;
    }

    public void setCommenter(User commenter) {
        this.commenter = commenter;
    }
}
