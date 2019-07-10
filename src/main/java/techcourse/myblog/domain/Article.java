package techcourse.myblog.domain;

public class Article {
    private static final String EMPTY_TEXT = "NULL";

    private final String title;
    private final String coverUrl;
    private final String contents;

    public Article(String title, String coverUrl, String contents) {
        validateTitle(title);
        validateContents(contents);
        this.title = title;
        this.contents = contents;
        this.coverUrl = (isBlank(coverUrl)) ? EMPTY_TEXT : coverUrl;
    }

    private void validateTitle(String title) {
        if (isBlank(title)) {
            throw new IllegalArgumentException("제목을 입력해주세요!");
        }
    }

    private void validateContents(String contents) {
        if (isBlank(contents)) {
            throw new IllegalArgumentException("내용을 입력해주세요!");
        }
    }

    private boolean isBlank(String text) {
        return text == null || "".equals(text);
    }
}
