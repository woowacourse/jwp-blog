package techcourse.myblog.dto.article;

import techcourse.myblog.domain.user.User;

public class ArticleResponse {
    private Long id;
    private String title;
    private String contents;
    private String coverUrl;
    private User author;

    public ArticleResponse() {
    }

    public ArticleResponse(Long id, String title, String contents, String coverUrl, User author) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
