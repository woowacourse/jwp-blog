package techcourse.myblog.service.assembler;

import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.dto.ResponseCommentDto;

public class CommentAssembler {

    public static Comment toEntity(CommentDto commentDto, User author, Article article) {
        return Comment.builder()
                .author(author)
                .contents(commentDto.getContents())
                .article(article)
                .build();
    }

    public static ResponseCommentDto toResponseDto(Comment comment) {
        return new ResponseCommentDto(comment.getId(), comment.getContents(), comment.getAuthor());
    }
}
