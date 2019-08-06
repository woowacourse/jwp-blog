package techcourse.myblog.user.domain.vo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<Password, String> {

    @Override
    public String convertToDatabaseColumn(Password password) {
        return password.getPassword();
    }

    @Override
    public Password convertToEntityAttribute(String password) {
        return Password.of(password);
    }
}