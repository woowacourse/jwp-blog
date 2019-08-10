package techcourse.myblog.article.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import techcourse.myblog.user.domain.User;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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
