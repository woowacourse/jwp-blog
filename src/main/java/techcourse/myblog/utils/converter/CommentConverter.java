package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentResponseDto;
import techcourse.myblog.dto.UserResponseDto;

import java.util.Date;

public class CommentConverter {
    public static CommentResponseDto toResponseDto(Comment comment) {
        Long id = comment.getId();
        UserResponseDto commenter = UserConverter.toResponseDto(comment.getCommenter());
        Date createdDate = comment.getCreatedDate();
        String contents = comment.getContents();

        return CommentResponseDto.of(id, commenter, createdDate, contents);
    }
}
