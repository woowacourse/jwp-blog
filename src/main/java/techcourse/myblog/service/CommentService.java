package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.ArticleException;
import techcourse.myblog.exception.CommentAuthenticationException;
import techcourse.myblog.exception.CommentException;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

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
                .orElseThrow(ArticleException::new);
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserException::new);
    }

    @Transactional
    public void update(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentFindById(commentId);
        comment.update(commentRequestDto);
    }

    private Comment getCommentFindById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentException::new);
    }

    @Transactional
    public void remove(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void checkAuthentication(UserResponseDto userResponseDto, Long commentId) {
        String userEmail = userResponseDto.getEmail();
        Comment comment = getCommentFindById(commentId);
        String commenterEmail = comment.getCommenter().getEmail();

        if (!userEmail.equals(commenterEmail)) {
            throw new CommentAuthenticationException();
        }
    }
}
