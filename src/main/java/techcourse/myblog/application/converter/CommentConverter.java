package techcourse.myblog.application.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.vo.CommentContents;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Configuration
public class CommentConverter {
    public List<CommentDto> toDtos(List<Comment> comments, String sessionEmail) {
        return comments.stream()
                .map(comment -> new CommentDto(comment.getId(), comment.getContents().getContents(), comment.getAuthor().getName(), null, comment.getAuthor().isMatchEmail(sessionEmail)))
                .collect(Collectors.toList());
    }

    public Comment toEntity(String commentContents, User author, Article article) {
        return new Comment(new CommentContents(commentContents), author, article);
    }

    public CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getContents().getContents(), comment.getAuthor().getName(), comment.getArticleId(), true);
    }
}
