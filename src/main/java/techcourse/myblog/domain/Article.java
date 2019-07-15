package techcourse.myblog.domain;

import java.util.Objects;
import java.util.StringJoiner;

public class Article {
    private static final String EMPTY = "";
    private static final String EMPTY_ARGUMENT = "입력 요소 중 일부가 비어 있습니다.";

    private int id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(final int id, final String title, final String coverUrl, final String contents) {
        this.id = id;
        this.title = trim(title);
        this.coverUrl = coverUrl;
        this.contents = trim(contents);
    }

    private String trim(final String string) {
        if (string == null || string.trim().equals(EMPTY)) {
            throw new IllegalArgumentException(EMPTY_ARGUMENT);
        }
        return string.trim();
    }

    public boolean isSameId(final int id) {
        return this.id == id;
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
    public String toString() {
        return new StringJoiner(", ", Article.class.getSimpleName() + "[", "]")
                .add("id: \"" + id + "\"")
                .add("title: \"" + title + "\"")
                .add("coverUrl: \"" + coverUrl + "\"")
                .add("contents: \"" + contents + "\"")
                .toString();
    }

    @Override
    public boolean equals(final Object another) {
        if (this == another) return true;
        if (!(another instanceof Article)) return false;
        final Article article = (Article) another;
        return id == article.id &&
                title.equals(article.title) &&
                coverUrl.equals(article.coverUrl) &&
                contents.equals(article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
