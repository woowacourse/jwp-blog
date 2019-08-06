package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.user.User;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment write(Article article, User user, String contents) {
        return commentRepository.save(new Comment(article, user, contents));
    }

    @Transactional
    public void tryUpdate(long commentId, String contents, User author) {
        commentRepository.findById(commentId).filter(comment -> comment.isSameAuthor(author))
                                            .ifPresent(comment -> comment.setContents(contents));
    }

    @Transactional
    public void delete(long commentId, User author) {
        commentRepository.findById(commentId).filter(comment -> comment.isSameAuthor(author))
                                            .ifPresent(comment -> commentRepository.deleteById(commentId));
    }
}