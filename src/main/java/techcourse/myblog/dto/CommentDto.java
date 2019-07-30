package techcourse.myblog.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class CommentDto {
    private User writer;
    private Article article;
    private String contents;

    public CommentDto(final User writer, final Article article, final String contents) {
        this.writer = writer;
        this.article = article;
        this.contents = contents;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(final User writer) {
        this.writer = writer;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(final Article article) {
        this.article = article;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(final String contents) {
        this.contents = contents;
    }
}
