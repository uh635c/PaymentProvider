package com.example.paymentprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("customers")
public class CustomerEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("country")
    private Country country;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updateAt;
    @Column("status")
    private Status status;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
