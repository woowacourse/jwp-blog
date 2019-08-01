package techcourse.myblog.comment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.repository.ArticleRepository;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.exception.CommentAuthenticationException;
import techcourse.myblog.comment.exception.NotFoundCommentException;
import techcourse.myblog.comment.repository.CommentRepository;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.exception.NotFoundUserException;
import techcourse.myblog.user.repository.UserRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @Transactional
    public Comment addComment(CommentRequestDto commentRequestDto, UserResponseDto userResponseDto, Long articleId) {
        String contents = commentRequestDto.getContents();
        User commenter = findUserByEmail(userResponseDto.getEmail());
        Article article = findArticleByArticleId(articleId);

        Comment comment = commentRepository.save(new Comment(contents, commenter, article));

        return comment;
    }

    private Article findArticleByArticleId(Long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundArticleException());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException());
    }

    @Transactional
    public void update(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentFindById(commentId);
        comment.update(commentRequestDto);
    }

    private Comment getCommentFindById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException());
    }

    @Transactional
    public void remove(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void checkAuthentication(Long commentId, UserResponseDto userResponseDto) {
        String userEmail = userResponseDto.getEmail();
        Comment comment = getCommentFindById(commentId);
        String commenterEmail = comment.getCommenter().getEmail();

        if (!userEmail.equals(commenterEmail)) {
            throw new CommentAuthenticationException();
        }
    }
}
