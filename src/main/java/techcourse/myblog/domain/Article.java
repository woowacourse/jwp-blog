package techcourse.myblog.domain;


public class Article {
    private static final String DEFAULT_COVER_URL = "/images/default/bg.jpg";
    private final String title;
    private final String contents;
    private final String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        if (coverUrl.isEmpty()) {
            this.coverUrl = DEFAULT_COVER_URL;
            return;
        }
        this.coverUrl = coverUrl;
    }

    public boolean isSameTitle(String title) {
        return title.equals(this.title);
    }

    public boolean isSameContents(String contents) {
        return contents.equals(this.contents);
    }

    public boolean isSameCoverUrl(String coverUrl) {
        return coverUrl.equals(this.coverUrl);
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title : \"" + title + "\"" +
                ", contents : \"" + contents + "\"" +
                ", coverUrl : \"" + coverUrl + "\"" +
                "}";
    }
}