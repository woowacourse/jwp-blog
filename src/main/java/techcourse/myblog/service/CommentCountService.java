package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.comment.CommentRepository;

@Service
public class CommentCountService {
    private CommentRepository commentRepository;

    public CommentCountService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public int countByArticleId(Long articleId) {
        return commentRepository.countByArticle_Id(articleId);
    }
}
