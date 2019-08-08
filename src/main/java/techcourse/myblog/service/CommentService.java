package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;

import java.util.Optional;
<<<<<<< HEAD

=======
>>>>>>> 6965e70b348d50fbf24db299dd625f4078160a6a
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Optional<Comment> find(long id) {
        return commentRepository.findById(id);
    }

    public Comment write(Article article, User user, String contents) {
        return commentRepository.save(new Comment(article, user, contents));
    }

    @Transactional
    public Comment tryUpdate(long commentId, String contents, User author) {
        return commentRepository.findById(commentId).filter(comment -> comment.isSameAuthor(author))
                                                    .map(comment -> comment.setContents(contents))
                                                    .orElse(null);
    }

    @Transactional
    public void delete(long commentId, User author) {
        commentRepository.findById(commentId).filter(comment -> comment.isSameAuthor(author))
                                            .ifPresent(comment -> commentRepository.deleteById(commentId));
    }
}