package techcourse.myblog.application.converter;

import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.Comment;


public class CommentConverter extends Converter<CommentDto, Comment> {
    private static CommentConverter converter = new CommentConverter();

    CommentConverter() {
        super(commentDto -> new Comment(commentDto.getContents(), commentDto.getAuthor(), commentDto.getArticle()),
                comment -> new CommentDto(comment.getId(), comment.getContents(), comment.getAuthor(), comment.getArticle()));
    }

    public static CommentConverter getInstance() {
        return converter;
    }
}
