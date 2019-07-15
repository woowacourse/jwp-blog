package techcourse.myblog.domain;

import techcourse.myblog.dto.ArticleRequestDto;

public class Article {
    private static long NEXT_ID = 1;

    private Long id;
    private String title;
    private String coverUrl;
    private String contents;
    private String category;

    private Article() {

    }

    public static Article of(ArticleRequestDto articleRequestDto) {
        Article newArticle = new Article();

        newArticle.id = NEXT_ID++;
        newArticle.title = articleRequestDto.getTitle();
        newArticle.coverUrl = articleRequestDto.getCoverUrl();
        newArticle.contents = articleRequestDto.getContents();
        newArticle.category = articleRequestDto.getCategory();

        return newArticle;
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

    public String getCategory() {
        return category;
    }

    public void update(ArticleRequestDto articleRequestDto) {
        this.title = articleRequestDto.getTitle();
        this.coverUrl = articleRequestDto.getCoverUrl();
        this.contents = articleRequestDto.getContents();
        this.category = articleRequestDto.getCategory();
    }

    public boolean hasCategory(String category) {
        return this.category.equals(category);
    }
}
