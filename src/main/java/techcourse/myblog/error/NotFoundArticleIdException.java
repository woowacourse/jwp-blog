package techcourse.myblog.error;

public class NotFoundArticleIdException extends RuntimeException {
    public NotFoundArticleIdException(String msg){
        super(msg);
    }
}
