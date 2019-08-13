package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    public ResponseEntity<String> handleArticleException(ArticleException e) {
        log.error(EXCEPTION, e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("index", "/");
        return new ResponseEntity<>("error", headers, HttpStatus.NOT_FOUND);
    }

    /**
     * 등록된 이메일이 없는 경우
     * 비밀번호가 틀린 경우
     *
     * @param e
     * @return 다시 로그인
     */
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> handleLoginException(LoginException e) {
        log.error(e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("login", "/login");
        return new ResponseEntity<>("error", headers, HttpStatus.NOT_FOUND);
    }

    /**
     * 이미 등록된 이메일일 경우
     * 회원 가입 정보를 다 입력하지 않았을 경우
     *
     * @param e
     * @return 다시 회원가입 페이지로
     */
    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<String> handleSignUpException(SignUpException e) {
        log.error(e.getMessage());
        HttpHeaders headers = new HttpHeaders();
        headers.add("signup", "/signup");
        return new ResponseEntity<>("error", headers, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 댓글을 수정 또는 삭제하려는 사용자가
     * 댓글 작성자가 아닌 경우
     *
     * @param e
     * @return 인덱스 페이지로
     */
    @ExceptionHandler(CommentAuthenticationException.class)
    public ResponseEntity<String> handleCommentAuthenticationException(CommentAuthenticationException e) {
        log.error(EXCEPTION, e.getMessage());
        return new ResponseEntity<>("error", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserException.class)
    public void handleUserException(UserException e) {
        log.error(EXCEPTION, e.getMessage());
    }
}
