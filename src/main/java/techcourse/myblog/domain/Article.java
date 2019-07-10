package techcourse.myblog.domain;

public class Article {
    private static long NEXT_ID = 1;

    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public static Article of(String title, String backgroundURL, String content) {
        Article newArticle = new Article();
        newArticle.setId(NEXT_ID++);
        newArticle.setTitle(title);
        newArticle.setCoverUrl(backgroundURL);
        newArticle.setContents(content);
        return newArticle;
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
