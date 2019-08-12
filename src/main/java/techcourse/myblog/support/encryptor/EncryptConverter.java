package techcourse.myblog.support.encryptor;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EncryptConverter implements AttributeConverter<String, String> {

    @Autowired
    EncryptHelper encryptHelper;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptHelper.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}
