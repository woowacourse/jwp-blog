package techcourse.myblog.domain;

public class Article {
    private static int nextId = 1;

    private int id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(String title, String coverUrl, String contents) {
        this.id = nextId++;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public void update(String newTitle, String newCoverUrl, String newContents) {
        this.title = newTitle;
        this.coverUrl = newCoverUrl;
        this.contents = newContents;
    }

    public boolean hasSameIdWith(int id) {
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
}
