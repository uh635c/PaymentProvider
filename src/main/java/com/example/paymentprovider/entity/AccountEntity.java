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
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("accounts")
public class AccountEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("currency")
    private Currency currency;
    @Column("balance")
    private Long balance;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updateAt;
    @Column("status")
    private Status status;
    @Column("merchant_id")
    private String merchantId;

    @Transient
    private List<TransactionEntity> transactions;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
