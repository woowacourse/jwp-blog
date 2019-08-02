package techcourse.myblog.dto;

public class ArticleResponse {
    private long articleId;
    private String title;
    private String coverUrl;
    private String contents;
    private int countOfComment;

    public ArticleResponse(long articleId, String title, String coverUrl, String contents, int countOfComment) {
        this.articleId = articleId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
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

    public int getCountOfComment() {
        return countOfComment;
    }
}
