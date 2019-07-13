package techcourse.myblog.domain;

import java.util.Objects;

public class Article {
    private static final int INIT_ARTICLE_ID = 1;
    private static int latestId = INIT_ARTICLE_ID;

    private final int id;
    private String title;
    private String coverUrl;
    private String contents;

    public Article(final String title, final String coverUrl, final String contents) {
        checkNull(title, coverUrl, contents);
        this.id = latestId++;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Article(final int id, final String title, final String coverUrl, final String contents) {
        checkNull(title, coverUrl, contents);
        if(id < INIT_ARTICLE_ID) {
            throw new IllegalArgumentException("적절한 ID가 아닙니다.");
        }
        this.id = id;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    private void checkNull(String title, String coverUrl, String contents) {
        if (Objects.isNull(title) || Objects.isNull(coverUrl) || Objects.isNull(contents)) {
            throw new NullPointerException();
        }
    }

    public boolean match(final int id) {
        return (this.id == id);
    }

    public void update(final Article article) {
        if (Objects.isNull(article)) {
            throw new NullPointerException();
        }
        this.title = article.getTitle();
        this.coverUrl = article.getCoverUrl();
        this.contents = article.getContents();
    }

    public ArticleDTO toConvertDTO() {
        ArticleDTO articleDTO = new ArticleDTO(title, coverUrl, contents);
        articleDTO.setId(id);
        return articleDTO;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id &&
                Objects.equals(title, article.title) &&
                Objects.equals(coverUrl, article.coverUrl) &&
                Objects.equals(contents, article.contents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, coverUrl, contents);
    }
}
