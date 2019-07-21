package techcourse.myblog.dto;

public class ArticleResponseDto {
    private Long id;
    private String title;
    private String coverUrl;
    private String contents;

    public ArticleResponseDto(Long id, String title, String coverUrl, String contents) {
        this.id = id;
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
}
