package techcourse.myblog.comment.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.domain.CommentRepository;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentResponseDto;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.comment.exception.NotFoundCommentException;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Comment save(long articleId, long authorId, CommentCreateDto commentCreateDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new NotFoundArticleException(articleId));
        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundUserException(authorId));
        return commentRepository.save(commentCreateDto.toComment(author, article));
    }

    public List<CommentResponseDto> findAllByArticleId(long articleId) {
        List<Comment> comments = (List<Comment>) commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

    public CommentResponseDto update(long commentId, long authorId, CommentUpdateDto commentUpdateDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException(commentId));
        Comment updatedComment = comment.updateComment(commentUpdateDto.getContents(), authorId);
        return modelMapper.map(updatedComment, CommentResponseDto.class);
    }

    public boolean deleteById(long commentId, long authorId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException(commentId));
        comment.deleteValidation(authorId);
        commentRepository.deleteById(commentId);
        return true;
    }

    public CommentResponseDto findById(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException(commentId));
        return modelMapper.map(comment, CommentResponseDto.class);
    }
}
