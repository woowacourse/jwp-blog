package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.comment.CommentRepository;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.user.User;

import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(CommentDto commentDto, Article article, User author) {
        Comment comment = commentDto.toEntity(article, author);
        return commentRepository.save(comment);
    }

    public List<Comment> findAll(Article article) {
        return commentRepository.findAllByArticle(article);
    }
}
