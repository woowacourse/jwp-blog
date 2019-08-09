package techcourse.myblog.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.ArticleResponseDto;
import techcourse.myblog.service.ArticleGenericService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

@RestController
@RequestMapping("/api/articles")
public class ApiArticleController {
    private final ArticleGenericService articleGenericService;

    public ApiArticleController(ArticleGenericService articleGenericService) {
        this.articleGenericService = articleGenericService;
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDto> read(@PathVariable long articleId,
                                                   @SessionInfo UserSessionInfo userSessionInfo) {
        ArticleResponseDto articleResponseDto = articleGenericService.findArticle(articleId, ArticleResponseDto.class);
        return new ResponseEntity<>(articleResponseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDto> create(@RequestBody ArticleDto articleDto,
                                                     @SessionInfo UserSessionInfo userSessionInfo) {
        ArticleResponseDto articleResponseDto = articleGenericService.add(articleDto, userSessionInfo.toUser(), ArticleResponseDto.class);
        return new ResponseEntity<>(articleResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{articleId}")
    public ResponseEntity<ArticleResponseDto> update(@PathVariable long articleId,
                                                     @RequestBody ArticleDto articleDto,
                                                     @SessionInfo UserSessionInfo userSessionInfo) {
        ArticleResponseDto articleResponseDto = articleGenericService.update(articleId, articleDto, userSessionInfo.toUser(), ArticleResponseDto.class);
        return new ResponseEntity<>(articleResponseDto, HttpStatus.ACCEPTED);
    }
}
