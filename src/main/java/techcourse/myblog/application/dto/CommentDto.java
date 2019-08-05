package techcourse.myblog.application.dto;

import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;

public class CommentDto {
    private Long id;
    private String contents;
    private String authorName;
    private Boolean matchAuthor;

    public CommentDto(Long id, String contents, String authorName, Boolean matchAuthor) {
        this.id = id;
        this.contents = contents;
        this.authorName = authorName;
        this.matchAuthor = matchAuthor;
    }

    public String getContents() {
        return contents;
    }

    public Long getId() {
        return id;
    }

    public Boolean getMatchAuthor() {
        return matchAuthor;
    }

    public String getAuthorName() {
        return authorName;
    }
}
