package techcourse.myblog.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.service.ArticleService;

@RestController
@RequestMapping("/api/articles/counts")
public class ArticleRestController {
    private static final Logger log = LoggerFactory.getLogger(ArticleRestController.class);

    private final ArticleService articleService;

    public ArticleRestController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<Integer> getCount(@RequestParam(value = "author", required = false) String author) {
        log.debug("author:{}", author);
        int count = articleService.findAllByAuthor(author).size();
        return new ResponseEntity<>(count, HttpStatus.FOUND);
    }
}
