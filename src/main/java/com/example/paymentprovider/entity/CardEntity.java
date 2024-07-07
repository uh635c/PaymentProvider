package com.example.paymentprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("cards")
public class CardEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("card_number")
    private Long cardNumber;
    @Column("expiration_date")
    private LocalDate expirationDate;
    @Column("cvv")
    private Long cvv;
    @Column("currency")
    private Currency currency;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("updated_at")
    private LocalDateTime updateAt;
    @Column("status")
    private Status status;
    @Column("customer_id")
    private String customerId;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
