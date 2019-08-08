package techcourse.myblog.service.dto;

public class CommentResponse {
    private String userName;
    private String contents;
    private Long id;

    public CommentResponse(String userName, String contents, Long id) {
        this.userName = userName;
        this.contents = contents;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
