package techcourse.myblog.dto;

public class ArticleResponse {
    private long articleId;
    private String title;
    private String coverUrl;
    private String contents;
    private String author;
    private String email;
    private int countOfComment;

    public ArticleResponse(long articleId, String title, String coverUrl, String contents, String author, String email, int countOfComment) {
        this.articleId = articleId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
        this.author = author;
        this.email = email;
        this.countOfComment = countOfComment;
    }

    public long getArticleId() {
        return articleId;
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

    public String getAuthor() {
        return author;
    }

    public String getEmail() {
        return email;
    }

    public int getCountOfComment() {
        return countOfComment;
    }
}
