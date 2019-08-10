package techcourse.myblog.utils;

import techcourse.myblog.application.dto.CommentRequestDto;

public class CommentTestObjects {
    public static final CommentRequestDto COMMENT_DTO = new CommentRequestDto("contents");
    public static final CommentRequestDto UPDATE_COMMENT_DTO = new CommentRequestDto("new-contents");
    public static final CommentRequestDto BLANK_COMMENT_DTO = new CommentRequestDto("");
    public static final CommentRequestDto NULL_COMMENT_DTO = new CommentRequestDto(null);
}
