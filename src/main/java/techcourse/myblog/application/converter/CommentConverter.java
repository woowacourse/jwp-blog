package techcourse.myblog.application.converter;

import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {
    public List<CommentDto> toDto(List<Comment> comments, String sessionEmail) {
        return comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getContents().getContents(), comment.getAuthor().getName(), comment.getAuthor().compareEmail(sessionEmail)))
                .collect(Collectors.toList());
    }
}
