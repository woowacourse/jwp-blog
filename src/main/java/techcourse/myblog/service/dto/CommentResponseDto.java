package techcourse.myblog.service.dto;

import java.util.Date;

public class CommentResponseDto {
    private Long id;
    private UserResponseDto commenter;
    private Date createdDate;
    private String contents;

    private CommentResponseDto() {
    }

    private CommentResponseDto(Long id, UserResponseDto commenter, Date createdDate, String contents) {
        this.id = id;
        this.commenter = commenter;
        this.createdDate = createdDate;
        this.contents = contents;
    }

    public static CommentResponseDto of(Long id, UserResponseDto commenter, Date createdDate, String contents) {
        return new CommentResponseDto(id, commenter, createdDate, contents);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserResponseDto getCommenter() {
        return commenter;
    }

    public void setCommenter(UserResponseDto commenter) {
        this.commenter = commenter;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "CommentResponseDto{" +
                "id=" + id +
                ", commenter=" + commenter +
                ", createdDate=" + createdDate +
                ", contents='" + contents + '\'' +
                '}';
    }
}
