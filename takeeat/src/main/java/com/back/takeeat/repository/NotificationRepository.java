package com.back.takeeat.repository;

import com.back.takeeat.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByMemberId(Long memberId);

    boolean existsByMemberIdAndWatched(Long memberId, Boolean watched);
}
