package techcourse.myblog.domain;

public class Article {
    private static int currentId = 1;
    private final int id;
    private String title;
    private String contents;
    private String coverUrl;

    public Article(String title, String contents, String coverUrl) {
        this.title = title;
        this.contents = contents;
        this.coverUrl = coverUrl;
        this.id = currentId++;
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

    public int getId() {
        return id;
    }


}
