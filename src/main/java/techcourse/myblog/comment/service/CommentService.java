package techcourse.myblog.comment.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.domain.CommentRepository;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentResponseDto;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.comment.exception.NotFoundCommentException;
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
        Comment comment = commentRepository.save(commentDto.toComment(userService.findById(authorId), articleService.findById(articleId)));
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    public Comment findById(long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NotFoundCommentException(commentId));
    }

    public CommentResponseDto find(long commentId) {
        Comment comment = findById(commentId);
        return modelMapper.map(comment, CommentResponseDto.class);
    }

    public List<CommentResponseDto> findAllByArticleId(long articleId) {
        List<Comment> comments = (List<Comment>) commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentResponseDto.class))
                .collect(Collectors.toList());
    }

    public CommentResponseDto update(long commentId, long authorId, CommentUpdateDto commentDto) {
        Comment comment = findById(commentId);
        userService.checkMatchUser(comment.getAuthor(), authorId);
        return modelMapper.map(comment.updateComment(commentDto.getContents()), CommentResponseDto.class);
    }

    public void delete(long commentId, long authorId) {
        Comment comment = findById(commentId);
        userService.checkMatchUser(comment.getAuthor(), authorId);
        commentRepository.delete(comment);
    }
}
