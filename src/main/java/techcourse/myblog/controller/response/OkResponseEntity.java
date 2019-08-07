package techcourse.myblog.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import techcourse.myblog.controller.message.Message;

public class OkResponseEntity extends ResponseEntity<Message> {
    public static final String message = "success";

    public OkResponseEntity() {
        super(new Message(message), HttpStatus.OK);
    }
}
