package techcourse.myblog.domain.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.CommentRequest;

@Component
public class CommentAssembler {
    private final ArticleService articleService;

    public CommentAssembler(ArticleService articleService) {
        this.articleService = articleService;
    }

    public Comment toCommentFromCommentRequestAndCommenter(CommentRequest commentRequest, User commenter) {
        return new Comment(commentRequest.getContents(),
                articleService.findById(commentRequest.getArticleId()),
                commenter);
    }
}
