package techcourse.myblog.application.converter;

import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.dto.CommentJsonDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.vo.CommentContents;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentConverter {
    public List<CommentDto> toDto(List<Comment> comments, String sessionEmail) {
        return comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getContents().getContents(), comment.getAuthor().getName(), comment.getAuthor().compareEmail(sessionEmail)))
                .collect(Collectors.toList());
    }

    public Comment toEntity(String commentContents, User author, Article article) {
        return new Comment(new CommentContents(commentContents), author, article);
    }

    public CommentJsonDto toCommentJsonDto(Comment savedComment) {
        return new CommentJsonDto(savedComment.getUserEmail(),savedComment.getCommentContents(), savedComment.getArticleId(),savedComment.getId());
    }
}
