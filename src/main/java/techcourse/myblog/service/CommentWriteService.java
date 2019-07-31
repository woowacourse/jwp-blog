package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.repository.CommentRepository;

@Service
@Transactional
public class CommentWriteService {
    private final CommentRepository commentRepository;
    
    public CommentWriteService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    
    public void save(Comment comment) {
        commentRepository.save(comment);
    }
    
    public void modify(Long commentId, Comment comment) {
        commentRepository.findById(commentId)
                .ifPresent(originalComment -> originalComment.update(comment));
    }
    
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
