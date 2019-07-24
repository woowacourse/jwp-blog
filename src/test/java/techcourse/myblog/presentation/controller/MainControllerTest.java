package techcourse.myblog.presentation.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.ArticleService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class MainControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @Test
    void 메인페이지_GET() throws Exception {
        String title1 = "myBlog";
        String coverUrl1 = "coverurl";
        String contents1 = "contents";
        String title2 = "blogtitle";
        String coverUrl2 = "blogcoverurl";
        String contents2 = "blogcontents";
        ArticleDto articleDto1 = new ArticleDto(1L, title1, coverUrl1, contents1);
        ArticleDto articleDto2 = new ArticleDto(2L, title2, coverUrl2, contents2);

        List<ArticleDto> articleDtos = Arrays.asList(articleDto1, articleDto2);
        given(articleService.findAll()).willReturn(articleDtos);
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(title1)))
                .andExpect(content().string(containsString(title2)))
                .andExpect(content().string(containsString(contents1)))
                .andExpect(content().string(containsString(contents2)))
                .andExpect(content().string(containsString(coverUrl1)))
                .andExpect(content().string(containsString(coverUrl2)));
    }

    @Test
    void Article_작성페이지_GET() throws Exception {
        mockMvc.perform(get("/writing"))
                .andExpect(status().isOk());
    }

    @Test
    void 로그인페이지_GET() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void 회원가입페이지_GET() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk());
    }
}
