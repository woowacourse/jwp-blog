package techcourse.myblog.domain;

public class NotFoundArticleIdException extends RuntimeException {
    public NotFoundArticleIdException(String msg){
        super(msg);
    }
}
