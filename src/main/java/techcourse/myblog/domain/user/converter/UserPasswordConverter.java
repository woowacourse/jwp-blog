package techcourse.myblog.domain.user.converter;

import techcourse.myblog.domain.user.UserPassword;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Deprecated
@Converter
public class UserPasswordConverter implements AttributeConverter<UserPassword, String> {
    @Override
    public String convertToDatabaseColumn(UserPassword attribute) {
        return attribute.getPassword();
    }

    @Override
    public UserPassword convertToEntityAttribute(String dbData) {
        return new UserPassword(dbData);
    }
}
