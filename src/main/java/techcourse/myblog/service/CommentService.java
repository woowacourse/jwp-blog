package techcourse.myblog.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.repository.CommentRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleService articleService;

    public Comment save(CommentSaveRequestDto commentSaveRequestDto, User user) {
        Long articleId = commentSaveRequestDto.getArticleId();
        Article article = articleService.findById(articleId);

        Comment comment = Comment.builder()
                .contents(commentSaveRequestDto.getComment())
                .article(article)
                .user(user)
                .build();
        return commentRepository.save(comment);
    }

    public List<Comment> findByArticleId(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
}
