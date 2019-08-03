package techcourse.myblog.application.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.repository.CommentRepository;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment write(Article article, User user, String contents) {
        return commentRepository.save(new Comment(article, user, contents));
    }

    public boolean tryUpdate(long commentId, String contents, User author) {
        return commentRepository.findById(commentId).map(comment -> {
            if (comment.isSameAuthor(author)) {
                comment.setContents(contents);
                return true;
            }
            return false;
        }).orElse(false);
    }

    public long tryDelete(long commentId, User author) {
        commentRepository.findById(commentId).ifPresent(comment -> {
            if (comment.isSameAuthor(author)) {
                commentRepository.deleteById(commentId);
            }
        });
        return commentId;
    }
}