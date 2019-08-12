package techcourse.myblog.service.dto;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleUserInput;
import techcourse.myblog.domain.user.User;

public class ArticleDto {
    private Long id;
    private Long userId;
    private String title;
    private String coverUrl;
    private String contents;
    private int commentCount;

    public ArticleDto(Long id, Long userId, String title, String coverUrl, String contents) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.coverUrl = coverUrl;
        this.contents = contents;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Article toEntity(User author) {
        ArticleUserInput articleUserInput = this.toVo();
        return new Article(author, articleUserInput);
    }

    public ArticleUserInput toVo() {
        return new ArticleUserInput(title, coverUrl, contents);
    }
}
