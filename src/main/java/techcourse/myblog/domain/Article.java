package techcourse.myblog.domain;

public class Article {
    private final Long articleId;
    private final String title;
    private final String content;
    private final String writer;
    private final String pictureSource;
    private final String writeDate;

    public Article(Long articleId, String title, String content, String writer, String pictureSource, String writeDate) {
        this.articleId = articleId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.pictureSource = pictureSource;
        this.writeDate = writeDate;
    }

    public Long getArticleId() {
        return articleId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriter() {
        return writer;
    }

    public String getPictureSource() {
        return pictureSource;
    }

    public String getWriteDate() {
        return writeDate;
    }
}
