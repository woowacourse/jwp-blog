package techcourse.myblog.utils;

import techcourse.myblog.service.dto.CommentDto;

public class CommentTestObjects {
    public static final CommentDto COMMENT_DTO = new CommentDto("contents");
    public static final CommentDto UPDATE_COMMENT_DTO = new CommentDto("new-contents");
    public static final CommentDto BLANK_COMMENT_DTO = new CommentDto("");
    public static final CommentDto NULL_COMMENT_DTO = new CommentDto(null);
}
