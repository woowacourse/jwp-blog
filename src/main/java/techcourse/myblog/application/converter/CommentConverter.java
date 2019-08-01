package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.Comment;


public class CommentConverter implements Converter<CommentDto, CommentDto, Comment> {
    private static CommentConverter converter = new CommentConverter();
    private UserConverter userConverter = UserConverter.getInstance();

    public static CommentConverter getInstance() {
        return converter;
    }

    @Override
    public Comment convertFromDto(CommentDto dto) {
        return new Comment(dto.getContents());
    }

    @Override
    public CommentDto convertFromEntity(Comment entity) {
        CommentDto commentDto = new CommentDto(entity.getId()
                , entity.getContents());
        commentDto.setAuthor(userConverter.convertFromEntity(entity.getAuthor()));
        return commentDto;
    }
}
