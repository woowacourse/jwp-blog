package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.exception.article.ArticleException;
import techcourse.myblog.exception.comment.CommentAuthenticationException;
import techcourse.myblog.exception.user.LoginException;
import techcourse.myblog.exception.user.SignUpException;
import techcourse.myblog.exception.user.UserException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);
    private static final String EXCEPTION = "Exception : {} ";

    /**
     * 없는 게시글(삭제된 게시글)에 접근했을 경우
     *
     * @param e
     * @return 인덱스 페이지로 이동
     */
    @ExceptionHandler(ArticleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleArticleException(ArticleException e) {
        log.error(EXCEPTION, e.getMessage());
        return "/";
    }

    /**
     * 등록된 이메일이 없는 경우
     * 비밀번호가 틀린 경우
     *
     * @param e
     * @return 다시 로그인
     */
    @ExceptionHandler(LoginException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleLoginException(LoginException e) {
        log.error(EXCEPTION, e.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.setViewName("/login");
        return mav;
    }

    /**
     * 이미 등록된 이메일일 경우
     * 회원 가입 정보를 다 입력하지 않았을 경우
     *
     * @param e
     * @return 다시 회원가입 페이지로
     */
    @ExceptionHandler(SignUpException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleSignUpException(SignUpException e) {
        log.error(EXCEPTION, e.getMessage());
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.setViewName("/signup");
        return mav;
    }

    /**
     * 댓글을 수정 또는 삭제하려는 사용자가
     * 댓글 작성자가 아닌 경우
     *
     * @param e
     * @return 인덱스 페이지로
     */
    @ExceptionHandler(CommentAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleCommentAuthenticationException(CommentAuthenticationException e) {
        log.error(EXCEPTION, e.getMessage());
        return "/";
    }

    @ExceptionHandler(UserException.class)
    public void handleUserException(UserException e) {
        log.error(EXCEPTION, e.getMessage());
    }
}
