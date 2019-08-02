package techcourse.myblog.web.controller;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.web.controller.common.ControllerTestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpMethod.*;
import static techcourse.myblog.utils.UserTestObjects.UPDATE_USER_DTO;

public class MypageControllerTests extends ControllerTestTemplate {
    @Test
    void 로그아웃상태_회원정보페이지_요청_리다이렉트() {
        httpRequest(GET, "/mypage").isFound();
    }
    
    @Test
    void 로그인_상태에서_회원정보페이지_요청시_성공() {
        loginAndRequestWriter(GET, "/mypage").isOk();
    }
    
    @Test
    void 로그아웃상태_회원_정보_수정_페이지_요청_리다이렉트() {
        httpRequest(GET, "/mypage/edit").isFound();
    }
    
    @Test
    void 로그인_상태에서_정보수정페이지_요청시_성공() {
        loginAndRequestWriter(GET, "/mypage/edit").isOk();
    }
    
    @Test
    void 로그인_상태에서_정보수정_결과_확인() {
        loginAndRequestWithDataWriter(PUT, "/mypage", parseUser(UPDATE_USER_DTO)).isFound();
        
        User savedUser = userRepository.findByEmail(savedUserDto.getEmail()).get();

        assertThat(savedUser.getName().equals(UPDATE_USER_DTO.getName())).isTrue();
        assertThat(savedUser.getName().equals(savedUserDto.getName())).isFalse();
    }
    
    @Test
    void 로그아웃상태_정보수정_불가() {
        UserDto update = new UserDto("newname", savedUserDto.getEmail(), savedUserDto.getPassword());
        httpRequestWithData(PUT, "/mypage", parseUser(update)).isFound();
        
        User savedUser = userRepository.findByEmail(savedUserDto.getEmail()).get();
        assertThat(savedUser.getName().equals(update.getName())).isFalse();
        assertThat(savedUser.getName().equals(savedUserDto.getName())).isTrue();
    }
    
    @Test
    void 로그아웃상태_탈퇴시도_실패() {
        String redirectUrl = getRedirectUrl(httpRequest(DELETE, "/mypage"));
        assertEquals(redirectUrl, "/login");
        assertThat(userRepository.findByEmail(savedUserDto.getEmail()).isPresent()).isTrue();
    }
    
    @Test
    void 로그인상태_자기자신_탈퇴시도_성공() {
        String redirectUrl = getRedirectUrl(loginAndRequestWriter(DELETE, "/mypage"));
        assertEquals(redirectUrl, "/logout");
        assertThat(userRepository.findByEmail(savedUserDto.getEmail()).isPresent()).isFalse();
    }
}
