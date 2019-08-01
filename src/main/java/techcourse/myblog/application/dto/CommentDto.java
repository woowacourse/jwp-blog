package techcourse.myblog.application.dto;

public class CommentDto {
    private Long id;
    private String contents;
    private UserResponseDto author;

    public CommentDto(Long id, String contents) {
        this.id = id;
        this.contents = contents;
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

    public UserResponseDto getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseDto user) {
        this.author = user;
    }

    public Boolean matchAuthor(String sessionEmail) {
        return getAuthor().matchEmail(sessionEmail);
    }
}
