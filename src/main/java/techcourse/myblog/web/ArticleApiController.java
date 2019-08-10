package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentsResponse;

@RestController
@RequestMapping("/api/articles")
public class ArticleApiController {
    private static final Logger log = LoggerFactory.getLogger(ArticleApiController.class);

    private ArticleService articleService;
    private CommentService commentService;

    public ArticleApiController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Article> selectArticle(@PathVariable("articleId") long articleId) {
        log.debug("begin");

        return new ResponseEntity<>(articleService.findById(articleId), HttpStatus.OK);
    }

    @GetMapping("/{articleId}/comments")
    public ResponseEntity<CommentsResponse> getComment(@PathVariable("articleId") long articleId) {
        log.debug("begin");

        return new ResponseEntity<>(commentService.findByArticleId(articleId), HttpStatus.OK);
    }
}
