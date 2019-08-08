package techcourse.myblog.comment.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.domain.CommentRepository;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentResponseDto;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.comment.exception.NotFoundCommentException;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }

    public CommentResponseDto save(long articleId, long authorId, CommentCreateDto commentDto) {
        Article article = articleService.findById(articleId);
        User author = userService.findById(authorId);
        Comment comment = commentRepository.save(commentDto.toComment(author, article));
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    public CommentResponseDto findById(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException(commentId));
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    public List<CommentResponseDto> findAllByArticleId(long articleId) {
        List<Comment> comments = (List<Comment>) commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

    public CommentResponseDto update(long commentId, long authorId, CommentUpdateDto commentDto) {
        Comment comment = checkAuthority(commentId, authorId);
        return modelMapper.map(comment.updateComment(commentDto.getContents()), CommentResponseDto.class);
    }

    public void delete(long commentId, long authorId) {
        Comment comment = checkAuthority(commentId, authorId);
        commentRepository.delete(comment);
    }

    private Comment checkAuthority(long commentId, long authorId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException(commentId));
        if (comment.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        return comment;
    }
}
