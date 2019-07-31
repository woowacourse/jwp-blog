package techcourse.myblog.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CommentDto {
    private long id;
    private String contents;
    private String userName;
    private LocalDate localDate;
    private LocalTime localTime;
}