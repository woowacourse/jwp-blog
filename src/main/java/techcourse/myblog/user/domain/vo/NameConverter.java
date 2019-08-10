package techcourse.myblog.user.domain.vo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class NameConverter implements AttributeConverter<Name, String> {
    @Override
    public String convertToDatabaseColumn(Name name) {
        return name.getName();
    }

    @Override
    public Name convertToEntityAttribute(String name) {
        return Name.of(name);
    }
}