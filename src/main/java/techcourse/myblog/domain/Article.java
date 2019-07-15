package techcourse.myblog.domain;

import java.util.StringJoiner;

public class Article {
    private String title;
    private String coverUrl;
    private String contents;

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(final String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Article.class.getSimpleName() + "[", "]")
                .add("title=\"" + title + "\"")
                .add("coverUrl=\"" + coverUrl + "\"")
                .add("contents=\"" + contents + "\"")
                .toString();
    }
}
