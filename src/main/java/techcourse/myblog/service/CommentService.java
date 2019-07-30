package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.dto.UserDto;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.exception.NotFoundUserException;
import techcourse.myblog.exception.NotMatchAuthorException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public List<Comment> findAllByArticle(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundArticleException::new);
        return commentRepository.findAllByArticle(article);
    }

    @Transactional
    public void update(UserDto.Response userDto, CommentDto.Update commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(NotFoundCommentException::new);
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);

        if (!comment.isWrittenBy(userDto.toUser())) {
            throw new NotMatchAuthorException();
        }

        comment.update(commentDto.toComment());
    }

    public void delete(UserDto.Response userDto, CommentDto.Delete commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId()).orElseThrow(NotFoundCommentException::new);
        User user = userRepository.findById(userDto.getId()).orElseThrow(NotFoundUserException::new);

        if (!comment.isWrittenBy(userDto.toUser())) {
            throw new NotMatchAuthorException();
        }

        commentRepository.deleteById(commentDto.getId());
    }
}
