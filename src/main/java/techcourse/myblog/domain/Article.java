package techcourse.myblog.domain;

public class Article {
    private int articleId;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(int articleId, String title, String coverUrl, String contents) {
        this.articleId = articleId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }
}
