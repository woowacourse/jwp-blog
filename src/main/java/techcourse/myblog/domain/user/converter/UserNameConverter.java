package techcourse.myblog.domain.user.converter;

import techcourse.myblog.domain.user.UserName;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Deprecated
@Converter
public class UserNameConverter implements AttributeConverter<UserName, String> {
    @Override
    public String convertToDatabaseColumn(UserName attribute) {
        return attribute.getUserName();
    }

    @Override
    public UserName convertToEntityAttribute(String dbData) {
        return new UserName(dbData);
    }
}
