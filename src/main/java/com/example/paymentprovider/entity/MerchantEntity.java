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
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table("merchants")
public class MerchantEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("name")
    private String name;
    @Column("secret_key")
    private String secretKey;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updateAt;
    @Column("status")
    private Status status;

    @Transient
    private List<AccountEntity> accounts;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
