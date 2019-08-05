package techcourse.myblog.application.dto;

public class CommentJsonDto {
    private String email;
    private String contents;
    private Long articleId;
    private Long id;

    private CommentJsonDto() {
    }

    public CommentJsonDto(String email, String contents, Long articleId, Long id) {
        this.email = email;
        this.contents = contents;
        this.articleId = articleId;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    public String getContents() {
        return contents;
    }


    public Long getArticleId() {
        return articleId;
    }
}
