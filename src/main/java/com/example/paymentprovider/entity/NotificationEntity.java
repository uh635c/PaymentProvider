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

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("notifications")
public class NotificationEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("attempt")
    private Integer attempt;
    @Column("url")
    private URI url;
    @Column("message")
    private String message;
    @Column("response_code")
    private Integer responseCode;
    @Column("response_body")
    private String responseBody;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("transaction_id")
    private String transactionId;

    @Override
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
