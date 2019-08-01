package techcourse.myblog.domain;

import techcourse.myblog.dto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class CommentAssembler {
    public static CommentDto writeDto(Comment comment) {
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setContents(comment.getContents());
        commentDto.setAuthor(comment.getAuthor());
        commentDto.setCreatedDate(comment.getCreatedDate());

        return commentDto;
    }

    public static List<CommentDto> writeDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentAssembler::writeDto)
                .collect(Collectors.toList());
    }

    public static Comment writeComment(CommentDto commentDto, User user) {
        return new Comment(commentDto.getContents(), user);
    }
}
