package com.example.paymentprovider.service.impl;

import com.example.paymentprovider.entity.NotificationEntity;
import com.example.paymentprovider.repository.NotificationRepository;
import com.example.paymentprovider.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public Mono<NotificationEntity> save(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

}
