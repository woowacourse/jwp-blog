package techcourse.myblog.domain;

import java.util.concurrent.atomic.AtomicLong;

public class Article {
    private static final AtomicLong NEXT_ID = new AtomicLong();

    private long id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents) {
        this.id = NEXT_ID.getAndIncrement();
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public boolean matchId(long id) {
        return this.id == id;
    }

    public void updateArticle(String newTitle, String newCoverUrl, String newContents) {
        this.title = newTitle;
        this.coverUrl = newCoverUrl;
        this.contents = newContents;
    }

    public long getId() {
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
}
