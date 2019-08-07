package techcourse.myblog.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import techcourse.myblog.controller.message.Message;

public class ErrorResponseEntity extends ResponseEntity<Message> {

    public ErrorResponseEntity(String message) {
        super(new Message(message), HttpStatus.BAD_REQUEST);
    }
}
