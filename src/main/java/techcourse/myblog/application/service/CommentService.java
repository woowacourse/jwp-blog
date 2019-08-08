package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.assembler.CommentAssembler;
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
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    private CommentAssembler commentAssembler = CommentAssembler.getInstance();

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Transactional
    public CommentDto save(CommentDto commentDto, Long articleId, User user) {
        Article article = articleService.findArticleById(articleId);

        Comment comment = new Comment.CommentBuilder()
                .article(article)
                .author(user)
                .contents(commentDto.getContents())
                .build();

        return commentAssembler.convertEntityToDto(commentRepository.save(comment));
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException("댓글이 존재하지 않습니다"));
    }

    @Transactional(readOnly = true)
    public List<CommentDto> findAllCommentsByArticleId(Long articleId) {
        Article article = articleService.findArticleById(articleId);
        List<Comment> comments = commentRepository.findByArticle(article);
        return comments.stream()
                .map(commentAssembler::convertEntityToDto)
                .collect(Collectors.toList());
    }

    public List<Boolean> matchAuthor(List<CommentDto> commentDtos, User user) {
        return commentDtos.stream()
                .map(commentDto -> commentDto.matchAuthor(user))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(long commentId, User user) {
        Comment comment = findCommentById(commentId);
        if (comment.isNotAuthor(user)){
            throw new NotMatchCommentAuthorException("댓글의 작성자가 아닙니다!");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public CommentDto modify(Long commentId, CommentDto commentDto, User user) {
        Comment comment = findCommentById(commentId);
        comment.modify(commentDto, user);
        return commentAssembler.convertEntityToDto(comment);
    }
}