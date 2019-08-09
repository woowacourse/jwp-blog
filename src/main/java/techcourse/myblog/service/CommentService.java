package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.Comments;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.CommentNotFoundException;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.web.annotation.LoginUser;

@Service
public class CommentService {
    private static final String COMMENT_ERROR = "댓글을 찾지 못했습니다.";

    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    @Autowired
    public CommentService(CommentRepository commentRepository, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
    }

    @Transactional
    public Comment save(CommentDto commentDto, User loginUser, long articleId) {
        Comment comment = convert(commentDto, loginUser, articleId);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comments findByArticle(long articleId) {
        Article article = articleService.findById(articleId);
        return new Comments(commentRepository.findCommentsByArticle(article));
    }

    @Transactional
    public Comment update(long commentId, User user, String contents) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_ERROR));
        comment.update(contents, user.getId());
        return comment;
    }

    private Comment convert(CommentDto commentDto, @LoginUser User loginUser, long articleId) {
        Article article = articleService.findById(articleId);
        return new Comment(commentDto.getContents(), loginUser, article);
    }

    @Transactional
    public void deleteById(long commentId, long loginUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_ERROR));
        comment.isAuthor(loginUserId);
        commentRepository.deleteById(commentId);
    }
}
