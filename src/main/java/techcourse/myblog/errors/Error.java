package techcourse.myblog.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Error {
    private String errorMessage;

    public Error(String message) {
        this.errorMessage = message;
    }
}
