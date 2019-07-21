package techcourse.myblog.domain;

public class ArticleResponseDto {
    private int id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleResponseDto(int id, String title, String coverUrl, String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public static ArticleResponseDto from(Article article) {
        return new ArticleResponseDto(article.getId(),
                article.getTitle(),
                article.getCoverUrl(),
                article.getContents());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Article toArticle() {
        return Article.createWithoutId(title, coverUrl, contents);
    }
}
