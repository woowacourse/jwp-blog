package techcourse.myblog.application.dto;

public class CommentDto {
    private Long id;
    private String contents;
    private String authorName;
    private Long articleId;
    private Boolean isAuthor;

    public CommentDto(Long id, String contents, String authorName, Long articleId, Boolean isAuthor) {
        this.id = id;
        this.contents = contents;
        this.authorName = authorName;
        this.articleId = articleId;
        this.isAuthor = isAuthor;
    }

    private CommentDto() {
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getContents() {
        return contents;
    }

    public Long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Boolean getAuthor() {
        return isAuthor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void setAuthor(Boolean author) {
        isAuthor = author;
    }
}
