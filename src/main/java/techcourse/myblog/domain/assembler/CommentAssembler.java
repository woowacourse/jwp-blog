package techcourse.myblog.domain.assembler;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.CommentResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CommentAssembler {
    public static CommentResponse writeDto(Comment comment) {
        CommentResponse commentDto = new CommentResponse(
                comment.getId(),
                comment.getContents(),
                comment.getAuthor(),
                comment.getCreatedDate()
        );

        return commentDto;
    }

    public static List<CommentResponse> writeDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentAssembler::writeDto)
                .collect(Collectors.toList());
    }

    public static Comment writeComment(CommentRequest commentDto, User user) {
        return new Comment(commentDto.getContents(), user);
    }
}
