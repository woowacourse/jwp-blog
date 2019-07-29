package techcourse.myblog.web.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;

public class CommentDto {
    private User writer;
    private Article article;
    private String contents;

    public CommentDto(User writer, String contents) {
        this.writer = writer;
        this.contents = contents;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public User getWriter() {
        return writer;
    }

    public String getContents() {
        return contents;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Comment create() {
        return new Comment(this.writer, this.contents, this.article);
    }

    @Override
    public String toString() {
        return "CommentDto{" +
                "writer=" + writer +
                ", contents='" + contents + '\'' +
                '}';
    }
}
