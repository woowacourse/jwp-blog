package techcourse.myblog.application.assembler;

import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.Comment;

public class CommentAssembler implements Assembler<Comment, CommentDto> {
    private static CommentAssembler assembler = new CommentAssembler();

    private UserAssembler userAssembler = UserAssembler.getInstance();

    private CommentAssembler() {
    }

    public static CommentAssembler getInstance() {
        return assembler;
    }

    @Override
    public CommentDto convertEntityToDto(Comment entity) {
        return new CommentDto(entity.getId()
                , entity.getContents()
                , userAssembler.convertEntityToDto(entity.getAuthor()));
    }
}