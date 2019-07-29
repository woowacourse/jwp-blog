package techcourse.myblog.comment.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.domain.ArticleRepository;
import techcourse.myblog.article.exception.NotFoundArticleException;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.domain.CommentRepository;
import techcourse.myblog.comment.dto.CommentDto;
import techcourse.myblog.comment.exception.NotFoundCommentException;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.domain.UserRepository;
import techcourse.myblog.user.exception.NotFoundUserException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public void save(long articleId, long authorId, CommentDto.Creation commentDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        User author = userRepository.findById(authorId).orElseThrow(NotFoundUserException::new);
        commentRepository.save(commentDto.toComment(author, article));
    }

    public List<CommentDto.Response> findAllByArticleId(long articleId) {
        List<Comment> comments = (List<Comment>) commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDto.Response.class))
                .collect(Collectors.toList());
    }

    public void update(long commentId, long authorId, CommentDto.Updation commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        if (comment.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        comment.updateComment(commentDto.getContents());
    }

    public void delete(long commentId, long authorId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
        if (comment.notMatchAuthorId(authorId)) {
            throw new NotMatchUserException();
        }
        commentRepository.deleteById(commentId);
    }
}
