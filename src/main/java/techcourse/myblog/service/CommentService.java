package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchAuthorException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<CommentDto.Response> findAllByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        List<Comment> comments = commentRepository.findAllByArticle(article);
        return comments.stream()
            .map(comment -> modelMapper.map(comment, CommentDto.Response.class))
            .collect(toList());
    }

    public void save(UserDto.Response userDto, CommentDto.Create commentDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);
        Article article = articleRepository.findById(commentDto.getArticleId()).orElseThrow(NotFoundArticleException::new);

        Comment comment = commentDto.toComment(user, article);

        commentRepository.save(comment);
    }

    @Transactional
    public void update(UserDto.Response userDto, CommentDto.Update commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(NotFoundCommentException::new);

        checkAuthor(userDto, comment);

        comment.update(commentDto.toComment());
    }

    public void delete(UserDto.Response userDto, CommentDto.Response commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(NotFoundCommentException::new);

        checkAuthor(userDto, comment);

        commentRepository.deleteById(commentDto.getId());
    }

    private void checkAuthor(UserDto.Response userDto, Comment comment) {
        if (!comment.isWrittenBy(userDto.toUser())) {
            throw new NotMatchAuthorException();
        }
    }
}
