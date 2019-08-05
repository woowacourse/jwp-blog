package techcourse.myblog.application.dto;

public class CommentJsonDto {
    private String email;
    private String contents;
    private Long articleId;
    private Long id;
    private Boolean isValidUser;

    private CommentJsonDto() {
    }

    public CommentJsonDto(String email, String contents, Long articleId, Long id, Boolean isValidUser) {
        this.email = email;
        this.contents = contents;
        this.articleId = articleId;
        this.id = id;
        this.isValidUser = isValidUser;
    }

    public Boolean getValidUser() {
        return isValidUser;
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
