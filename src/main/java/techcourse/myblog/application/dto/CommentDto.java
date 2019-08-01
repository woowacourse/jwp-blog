package techcourse.myblog.application.dto;

public class CommentDto {
    private Long id;
    private String contents;
    private UserResponseDto author;

    public CommentDto(Long id, String contents) {
        this.id = id;
        this.contents = contents;
    }

    public Boolean matchAuthor(String sessionEmail) {
        return getAuthor().matchEmail(sessionEmail);
    }

    public Long getId() {
        return id;
    }

    public String getContents() {
        return contents;
    }

    public UserResponseDto getAuthor() {
        return author;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setAuthor(UserResponseDto user) {
        this.author = user;
    }
}
