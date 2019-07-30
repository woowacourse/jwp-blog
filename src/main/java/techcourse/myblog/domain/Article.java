package techcourse.myblog.domain;

public class Article {
    private static final String EMPTY_TEXT = "NULL";

    private final int id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(int id, String title, String coverUrl, String contents) {
        validateTitle(title);
        validateContents(contents);
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.coverUrl = (isBlank(coverUrl)) ? EMPTY_TEXT : coverUrl;
    }

    private void validateTitle(String title) {
        if (isBlank(title)) {
            throw new InvalidArticleException("제목을 입력해주세요!");
        }
    }

    private void validateContents(String contents) {
        if (isBlank(contents)) {
            throw new InvalidArticleException("내용을 입력해주세요!");
        }
    }

    private boolean isBlank(String text) {
        return text == null || "".equals(text);
    }

    public boolean matchId(int articleId) {
        return this.id == articleId;
    }

    public void update(Article article) {
        title = article.getTitle();
        coverUrl = article.getCoverUrl();
        contents = article.getContents();
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

    public int getId() {
        return id;
    }
}
