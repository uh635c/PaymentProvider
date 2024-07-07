package com.example.paymentprovider.service;

import com.example.paymentprovider.entity.NotificationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface NotificationService {
    Mono<NotificationEntity> save(NotificationEntity notification);
}
