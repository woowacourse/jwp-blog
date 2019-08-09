package techcourse.myblog.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.Comments;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.exception.CommentNotFoundException;
import techcourse.myblog.domain.repository.CommentRepository;
import techcourse.myblog.dto.CommentDto;

@Service
public class CommentService {
    private static final String COMMENT_ERROR = "댓글을 찾지 못했습니다.";

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public CommentDto save(Comment comment) {
        Comment saveComment = commentRepository.save(comment);
        return modelMapper.map(saveComment, CommentDto.class);
    }

    @Transactional
    public void deleteById(long id, long loginUserId) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(COMMENT_ERROR));
        comment.isAuthor(loginUserId);
        commentRepository.deleteById(id);
    }

    @Transactional
    public CommentDto update(long commentId, User user, String contents) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CommentNotFoundException(COMMENT_ERROR));
        comment.update(contents, user.getId());

        return modelMapper.map(comment, CommentDto.class);
    }

    @Transactional
    public Comments findByArticle(Article article) {
        return new Comments(commentRepository.findCommentsByArticle(article));
    }
}
