package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.assembler.CommentAssembler;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.dto.ResponseCommentDto;
import techcourse.myblog.service.exception.NotFoundObjectException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment createComment(CommentDto commentDto, User user, Article article) {
        Comment comment = CommentAssembler.toEntity(commentDto, user, article);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundObjectException::new);
        comment.checkAvailableUserForDelete(user);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public Comment updateComment(Long commentId, User user, CommentDto commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(NotFoundObjectException::new);
        Comment updateComment = CommentAssembler.toEntity(commentDto, user, comment.getArticle());
        comment.update(updateComment);
        return comment;
    }

    public List<ResponseCommentDto> findAllComments(Long articleId) {
        List<ResponseCommentDto> commentDtos = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByArticleArticleId(articleId);

        for(Comment comment : comments) {
            commentDtos.add(CommentAssembler.toResponseDto(comment));
        }
        return commentDtos;
    }
}
