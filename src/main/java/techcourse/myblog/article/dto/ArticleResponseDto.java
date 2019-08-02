package techcourse.myblog.article.dto;

import lombok.Getter;
import lombok.Setter;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
public class ArticleResponseDto {
    private long id;
    private String title;
    private String coverUrl;
    private String contents;
    private User author;

    public boolean matchAuthorId(long authorId) {
        return author.getId() == authorId;
    }
}
