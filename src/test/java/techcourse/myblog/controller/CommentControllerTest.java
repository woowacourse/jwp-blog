//package techcourse.myblog.controller;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//
//import techcourse.myblog.controller.test.WebClientGenerator;
//import techcourse.myblog.domain.Article;
//import techcourse.myblog.dto.CommentDto;
//import techcourse.myblog.repository.ArticleRepository;
//import techcourse.myblog.service.ArticleService;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.http.HttpMethod.*;
//
//@ExtendWith(SpringExtension.class)
//class CommentControllerTest extends WebClientGenerator {
//
//    @Autowired
//    private ArticleRepository articleRepository;
//
//    private Article savedArticle;
//
//    @BeforeEach
//    public void setUp() {
//        Article article = new Article("제목", "url", "내용");
//        savedArticle = articleRepository.save(article);
//    }
//
//    @Test
//    public void 댓글_추가() {
//        CommentDto commentDto = new CommentDto("댓글입니다.");
//        responseSpec(POST, "/comments/" + savedArticle.getId(), parser(commentDto))
//                .expectStatus()
//                .isFound();
//    }
//
//    private MultiValueMap<String, String> parser(CommentDto commentDto) {
//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("contents", commentDto.getContents());
//        return multiValueMap;
//    }
//
//    @AfterEach
//    public void tearDown() {
//        articleRepository.deleteAll();
//    }
//}