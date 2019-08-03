package techcourse.myblog.application.dto;

public class ArticleDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    private UserResponseDto author;

    public ArticleDto(Long id, String title, String coverUrl, String contents, UserResponseDto author) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
    }

    public boolean matchEmail(String email) {
        return author.matchEmail(email);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public UserResponseDto getAuthor() {
        return author;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setAuthor(UserResponseDto author) {
        this.author = author;
    }
}