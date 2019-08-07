package techcourse.myblog.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

@RestController
@RequestMapping("/api/articles")
public class ApiArticleController {
    private final ArticleService articleService;

    public ApiArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<Article> read(@PathVariable long articleId,
                                        @SessionInfo UserSessionInfo userSessionInfo) {
        Article article = articleService.findArticle(articleId);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Article> create(@RequestBody ArticleDto.JSON json,
                                          @SessionInfo UserSessionInfo userSessionInfo) {
        Article article = articleService.add(json.toDto(), userSessionInfo.toUser());
        return new ResponseEntity<>(article, HttpStatus.CREATED);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<Article> update(@PathVariable long articleId,
                                          @RequestBody ArticleDto.JSON json,
                                          @SessionInfo UserSessionInfo userSessionInfo) {
        Article article = articleService.update(articleId, json.toDto(), userSessionInfo.toUser());
        return new ResponseEntity<>(article, HttpStatus.ACCEPTED);
    }
}
