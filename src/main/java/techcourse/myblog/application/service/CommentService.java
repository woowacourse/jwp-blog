package techcourse.myblog.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.ArticleConverter;
import techcourse.myblog.application.converter.CommentConverter;
import techcourse.myblog.application.converter.UserConverter;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.CommentNotFoundException;
import techcourse.myblog.application.service.exception.NotMatchCommentAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private static ArticleConverter articleConverter = ArticleConverter.getInstance();
    private static CommentConverter commentConverter = CommentConverter.getInstance();
    private static UserConverter userConverter = UserConverter.getInstance();

    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Transactional
    public void save(CommentDto commentDto, Long articleId, User user) {
        Article article = articleService.findArticleById(articleId);

        Comment comment = commentConverter.convertFromDto(commentDto);
        comment.init(user, article);

        commentRepository.save(comment);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다"));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllCommentsByArticleId(Long articleId) {
        Article article = articleService.findArticleById(articleId);
        List<Comment> comments = commentRepository.findByArticle(article);
        return commentConverter.createFromEntities(comments);
    }

    public List<Boolean> matchAuthor(List<CommentDto> commentDtos, User user) {
        return commentDtos.stream()
                .map(commentDto -> commentDto.matchAuthor(user))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(long commentId, User user) {
        Comment comment = findCommentById(commentId);
        if(!comment.getAuthor().equals(user)) {
            throw new NotMatchCommentAuthorException("댓글의 작성자가 아닙니다!");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void modify(Long commentId, CommentDto commentDto, User user) {
        Comment comment = findCommentById(commentId);
        if(!comment.getAuthor().equals(user)) {
            throw new NotMatchCommentAuthorException("댓글의 작성자가 아닙니다!");
        }
        comment.changeContent(commentDto);
    }
}