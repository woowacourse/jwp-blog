package techcourse.myblog.domain;

import java.util.Objects;

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
        this.title = article.title;
        this.contents = article.contents;
        this.coverUrl = article.coverUrl;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
