package techcourse.myblog.domain.user.UserConverter;

import techcourse.myblog.domain.user.UserEmail;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserEmailConverter implements AttributeConverter<UserEmail, String> {
    @Override
    public String convertToDatabaseColumn(UserEmail attribute) {
        return attribute.getEmail();
    }

    @Override
    public UserEmail convertToEntityAttribute(String dbData) {
        return new UserEmail(dbData);
    }
}
