package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class CommentDto {
    private Long id;
    private String contents;
    private User author;
    private Article article;
    private Boolean matchAuthor;

    public CommentDto(Long id, String contents, User author, Article article) {
        this.id = id;
        this.contents = contents;
        this.article = article;
        this.author = author;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Long getId() {
        return id;
    }

    public Boolean getMatchAuthor() {
        return matchAuthor;
    }

    public void matchAuthor(String email) {
        this.matchAuthor = author.compareEmail(email);
    }
}
