package techcourse.myblog.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import techcourse.myblog.domain.Comment;

@Getter
@AllArgsConstructor
public class CommentDTO implements DomainDTO<Comment> {
    private String contents;

    @Override
    public Comment toDomain() {
        return new Comment(contents);
    }
}
