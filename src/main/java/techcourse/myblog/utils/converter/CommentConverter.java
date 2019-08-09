package techcourse.myblog.utils.converter;

import techcourse.myblog.domain.Comment;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserResponseDto;

import java.util.Date;

public class CommentConverter {
    public static CommentResponseDto toResponseDto(Comment comment) {
        Long id = comment.getId();
        UserResponseDto commenter = UserConverter.toResponseDto(comment.getCommenter());
        Date createdDate = comment.getCreatedDate();
        String contents = comment.getContents();

        log.info("{} toResponseDto() >> ({}, {}, {}, {})", LOG_TAG, id, commenter.getName(), createdDate, contents);

        return CommentResponseDto.of(id, commenter, createdDate, contents);
    }
}
