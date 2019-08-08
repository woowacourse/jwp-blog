package techcourse.myblog.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.repository.CommentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentGenericService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleGenericService articleGenericService;
    private final ModelMapper modelMapper = new ModelMapper();

    public CommentGenericService(CommentRepository commentRepository, UserService userService, ArticleGenericService articleGenericService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleGenericService = articleGenericService;
    }


    public <T> T add(long articleId, User author, CommentDto commentDto, Class<T> type) {
        Article article = articleGenericService.findArticle(articleId, Article.class);
        User user = userService.getUserByEmail(author.getEmail());
        Comment comment = commentDto.toEntity(article, user);
        return modelMapper.map(commentRepository.save(comment), type);
    }

    public void delete(long commentId, User author) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        if (!comment.isAuthor(author)) {
            throw new CommentException("당신은 죽을수도 있습니다.");
        }
        commentRepository.deleteById(commentId);
    }

    public <T> T update(long commentId, User user, CommentDto commentDto, Class<T> type) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentException::new);
        comment.updateContents(commentDto.getContents(), user);
        return modelMapper.map(comment, type);
    }

    public <T> List<T> findByArticle(long articleId, Class<T> type) {
        Article article = articleGenericService.findArticle(articleId, Article.class);
        return article.getComments().stream()
                .map(comment -> modelMapper.map(comment, type))
                .collect(Collectors.toList());
    }
}
