package techcourse.myblog.domain;


public class Article implements Cloneable {
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

    ArticleDto createDto() {
        ArticleDto articleDto = new ArticleDto();
        articleDto.setTitle(title);
        articleDto.setContents(contents);
        articleDto.setCoverUrl(coverUrl);
        return articleDto;
    }

    boolean isSameTitle(String title) {
        return title.equals(this.title);
    }

    boolean isSameContents(String contents) {
        return contents.equals(this.contents);
    }

    boolean isSameCoverUrl(String coverUrl) {
        return coverUrl.equals(this.coverUrl);
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}