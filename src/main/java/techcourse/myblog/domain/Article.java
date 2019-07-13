package techcourse.myblog.domain;

public class Article {
    private static int latestId = 1;

    private final int id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(final String title, final String coverUrl, final String contents) {
        this.id = latestId++;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article(final int id, final String title, final String coverUrl, final String contents) {
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public int getId() {
        return id;
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

    public void update(final Article article) {
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
    }

    public ArticleDTO toConvertDTO() {
        ArticleDTO articleDTO = new ArticleDTO(title, coverUrl, contents);
        articleDTO.setId(id);
        return articleDTO;
    }
}
