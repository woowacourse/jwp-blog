package techcourse.myblog.domain;

public class Article {
    private static long NEXT_ID = 1;

    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    private String category;

    private Article(){

    }

    public static Article of(String title, String backgroundURL, String content) {
        return of(title, backgroundURL, content, "");
    }

    public static Article of(String title, String coverUrl, String contents, String category) {
        Article newArticle = new Article();

        newArticle.id = NEXT_ID++;
        newArticle.title = title;
        newArticle.coverUrl = coverUrl;
        newArticle.contents = contents;
        newArticle.category = category;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void update(Article article) {
        this.title = article.title;
        this.coverUrl = article.coverUrl;
        this.contents = article.contents;
        this.category = article.category;
    }
}
