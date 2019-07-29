package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment write(Article article, User user, String contents) {
        return commentRepository.save(new Comment(article,user,contents));
    }

    public boolean tryUpdate(long commentId, Comment toUpdate) {
        return commentRepository.findById(commentId).map(ifExists -> {
                                                            toUpdate.setId(commentId);
                                                            commentRepository.save(toUpdate);
                                                            return true;
                                                        }).orElse(false);
    }

    public void delete(long commentId) {
        commentRepository.deleteById(commentId);
    }
}