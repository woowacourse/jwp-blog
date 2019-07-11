package techcourse.myblog.domain;


public class Article {
    static final String DEFAULT_BACKGROUND = "/images/default/bg.jpg";
    private String title;
    private String contents;
    private String background;

    public Article(String title, String contents, String background) {
        this.title = title;
        this.contents = contents;
        this.background = background;
        if (this.background.isEmpty()) {
            this.background = DEFAULT_BACKGROUND;
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", background='" + background + '\'' +
                '}';
    }
}