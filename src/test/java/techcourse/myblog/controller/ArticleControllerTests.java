package techcourse.myblog.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import techcourse.myblog.controller.template.ControllerTestTemplate;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.service.dto.ArticleDto;

public class ArticleControllerTests extends ControllerTestTemplate {
    @Autowired
    private ArticleWriteService articleWriteService;
    private ArticleDto articleDto;
    private Article article;

    @BeforeEach
    void 게시글_작성_확인() {
        articleDto = new ArticleDto("1", "https://www.naver.com", "2");
        article = articleWriteService.save(articleDto);
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.POST, "/articles", bodyInsert(articleDto)), HttpStatus.FOUND, ".*articles.*");
    }

    @Test
    void index_페이지_조회() {
        checkStatus(HttpMethod.GET, "/", HttpStatus.OK);
    }

    @Test
    void 게시글_작성_페이지_확인() {
        checkStatus(HttpMethod.GET, "/writing", HttpStatus.OK);
    }

    @Test
    void 게시글_조회() {
        checkStatus(HttpMethod.GET, "/articles/" + article.getId(), HttpStatus.OK);
    }

    @Test
    void 게시글_수정_페이지_확인() {
        checkStatus(HttpMethod.GET, "/articles/" + article.getId() + "/edit", HttpStatus.OK);
    }

    @Test
    void 게시글_수정_확인() {
        checkStatusAndHeaderLocation(responseWithData(HttpMethod.PUT, "/articles/" + article.getId(), bodyInsert(articleDto)),
                HttpStatus.FOUND,".*articles.*");
    }

    @AfterEach
    void 게시글_삭제_확인() {
        checkStatusAndHeaderLocation(response(HttpMethod.DELETE, "/articles/" + article.getId())
                , HttpStatus.FOUND, ".*/");
    }
    
    protected MultiValueMap<String, String> bodyInsert(ArticleDto articleDto) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", articleDto.getTitle());
        multiValueMap.add("coverUrl", articleDto.getCoverUrl());
        multiValueMap.add("contents", articleDto.getContents());
        return multiValueMap;
    }
}