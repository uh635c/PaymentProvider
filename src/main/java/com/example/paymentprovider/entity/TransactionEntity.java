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

import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("transactions")
public class TransactionEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("type")
    private TransactionType type;
    @Column("payment_method")
    private MethodType paymentMethod;
    @Column("amount")
    private Long amount;
    @Column("language")
    private Language language;
    @Column("url")
    private URI url;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updatedAt;
    @Column("status")
    private TransactionStatus status;
    @Column("card_id")
    private String cardId;
    @Column("account_id")
    private String accountId;

    @Transient
    private CardEntity card;
    @Transient
    private CustomerEntity customer;
//    @Transient
//    private AccountEntity account;
//    @Transient
//    private List<NotificationEntity> notifications;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
