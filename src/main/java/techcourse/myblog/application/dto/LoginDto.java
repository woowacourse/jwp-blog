package techcourse.myblog.application.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto {
    @NotBlank
    private String email;
    
    @NotBlank
    private String password;
}
