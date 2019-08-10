package techcourse.myblog.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.converter.CommentConverter;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.exception.CommentNotFoundException;
import techcourse.myblog.application.service.exception.NotExistArticleIdException;
import techcourse.myblog.application.service.exception.NotMatchAuthorException;
import techcourse.myblog.domain.*;
import techcourse.myblog.domain.vo.CommentContents;

import java.util.List;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;
    private CommentConverter commentConverter;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService, CommentConverter commentConverter) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
        this.commentConverter = commentConverter;
    }

    @Transactional
    public void save(CommentContents commentContents, Long articleId, Email email) {
        Article article = articleService.findById(articleId);
        User user = userService.findUserByEmail(email.getEmail());
        Comment comment = new Comment(commentContents, user, article);

        commentRepository.save(comment);
    }

    public List<CommentDto> findAllCommentsByArticleId(Long articleId, String sessionEmail) {
        Article article = articleService.findById(articleId);
        List<Comment> comments = commentRepository.findByArticle(article);
        return commentConverter.toDtos(comments, sessionEmail);
    }

    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void modify(Long commentId, CommentContents commentContents) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다"));
        comment.updateContents(commentContents);
    }

    public void checkAuthor(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotExistArticleIdException("존재하지 않는 Article 입니다."));
        User author = comment.getAuthor();
        if (author.isNotMatchEmail(email)) {
            throw new NotMatchAuthorException("너는 이 글에 작성자가 아니다.");
        }
    }

    public CommentDto saveComment(CommentDto commentDto, String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        Article article = articleService.findById(commentDto.getArticleId());
        Comment comment = commentConverter.toEntity(commentDto.getContents(), user, article);
        Comment savedComment = commentRepository.save(comment);
        return commentConverter.toDto(savedComment);
    }

    @Transactional
    public CommentDto update(CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentDto.getId())
                .orElseThrow(IllegalArgumentException::new);
        comment.updateContents(new CommentContents(commentDto.getContents()));
        return commentConverter.toDto(comment);
    }
}
