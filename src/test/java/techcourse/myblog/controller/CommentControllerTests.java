package techcourse.myblog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import techcourse.myblog.MyblogApplicationTests;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.controller.dto.UserDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebTestClient
@WebMvcTest //컨트롤러, 인터셉터 빈을 등록해서 사용한다.
public class CommentControllerTests extends MyblogApplicationTests {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private Article article;
    private User user;
    private MockHttpSession session;
    private Comment comment;
    @MockBean
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        session = new MockHttpSession();
        user = new UserDto(USER_ID, USER_NAME, USER_PASSWORD, USER_EMAIL).toUser();
        article = new ArticleDto(ARTICLE_ID, ARTICLE_TITLE, ARTICLE_CONTENTS, ARTICLE_COVER_URL).toArticle(user);
        session.setAttribute("user", user);
        comment = new Comment(COMMENT_CONTENTS, user, article);
        given(commentRepository.findById(1L)).willReturn(Optional.of(comment));
    }

    @Test
    void comment_save() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/comment").session(session).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("contents", "abc")
                .param("articleId", "1"))
                .andDo(print())
                .andExpect(status()
                        .isFound())
                .andReturn();
        System.out.println(mvcResult.getResponse().getHeader("Location"));
        assertThat(mvcResult.getResponse().getHeader("Location").contains("articles")).isTrue();
    }

    @Test
    void comment수정페이지_접근() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/comment/1/edit").session(session).contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertThat(mvcResult.getResponse().getContentAsString().contains(COMMENT_CONTENTS)).isTrue();
    }

    @Test
    void comment수정() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/comment").session(session).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("articleId", String.valueOf(ARTICLE_ID))
                .param("contents", "updatedContents"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location").contains("articles")).isTrue();
    }

    @Test
    void comment삭제() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/comment").session(session).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("articleId", String.valueOf(ARTICLE_ID))
                .param("contents", COMMENT_CONTENTS))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andReturn();
        assertThat(mvcResult.getResponse().getHeader("Location").contains("articles")).isTrue();
    }

}
