package techcourse.myblog.domain.User;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
