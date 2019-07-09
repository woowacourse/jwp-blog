package techcourse.myblog.domain;

public class Article {
    private static long NEXT_ID = 1;

    private Long id;
    private String title;
    private String backgroundURL;
    private String content;

    public static Article of(String title, String backgroundURL, String content) {
        Article newArticle = new Article();
        newArticle.setId(NEXT_ID++);
        newArticle.setTitle(title);
        newArticle.setBackgroundURL(backgroundURL);
        newArticle.setContent(content);
        return newArticle;
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public String getBackgroundURL() {
        return backgroundURL;
    }

    private void setBackgroundURL(String backgroundURL) {
        this.backgroundURL = backgroundURL;
    }

    public String getContent() {
        return content;
    }

    private void setContent(String content) {
        this.content = content;
    }
}
