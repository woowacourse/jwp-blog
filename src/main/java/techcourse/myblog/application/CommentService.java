package techcourse.myblog.application;

import org.springframework.stereotype.Service;
import techcourse.myblog.application.assembler.CommentAssembler;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.application.exception.CommentNotFoundException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;
    private final CommentAssembler commentAssembler;

    public CommentService(CommentRepository commentRepository, ArticleService articleService,
                          UserService userService, CommentAssembler commentAssembler) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userService = userService;
        this.commentAssembler = commentAssembler;
    }

    public CommentResponse save(CommentRequest commentRequest, Long articleId, Long userId) {
        User author = userService.findById(userId);
        Article article = articleService.findById(articleId);

        Comment comment = new Comment(commentRequest.getContents(), author, article);
        commentRepository.save(comment);

        CommentResponse commentResponse = commentAssembler.convertToResponse(comment, author);
        return commentResponse;
    }

    public List<CommentResponse> findAllByArticle(Long articleId) {
        Article article = articleService.findById(articleId);
        return commentRepository.findAllByArticle(article)
                .stream()
                .map(comment -> {
                    User author = userService.findById(comment.getAuthor().getId());
                    return commentAssembler.convertToResponse(comment, author);
                })
                .collect(Collectors.toList())
                ;
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다."));
    }

    @Transactional
    public CommentResponse modify(Long commentId, UserResponse userResponse, CommentRequest commentRequest) {
        Comment comment = findById(commentId);
        User author = userService.findById(userResponse.getId());

        try {
            comment.updateContents(commentRequest.getContents(), author);
        } catch (IllegalArgumentException e) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 수정할 수 있습니다.");
        }

        CommentResponse commentResponse = commentAssembler.convertToResponse(comment, author);
        return commentResponse;
    }

    public void remove(Long commentId, UserResponse userResponse) {
        Comment comment = findById(commentId);
        User author = userService.findById(userResponse.getId());

        if (!comment.isSameAuthor(author)) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }
}
