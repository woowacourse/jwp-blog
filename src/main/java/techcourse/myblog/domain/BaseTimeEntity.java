package techcourse.myblog.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    @Column(name = "CREATED_DATE_TIME")
    private LocalDateTime createdDateTIme;

    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE_TIME")
    private LocalDateTime lastModifiedDateTime;

    public LocalDateTime getCreatedDateTime() {
        return createdDateTIme;
    }

    public LocalDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public LocalDate toCreatedDate() {
        return createdDateTIme.toLocalDate();
    }

    public LocalDate toLastModifiedDate() {
        return lastModifiedDateTime.toLocalDate();
    }
}
