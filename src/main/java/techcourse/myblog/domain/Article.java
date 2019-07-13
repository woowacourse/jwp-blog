package techcourse.myblog.domain;

import lombok.Builder;

@Builder
public class Article {
    private static long articleId = 1;

    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    private Article(long id, String title, String coverUrl, String contents) {
        this.id = id == 0 ? articleId++ : id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
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

    public boolean equals(long articleId) {
        return this.id == articleId;
    }
}
