package com.example.poetvine.server.repository;

import com.example.poetvine.server.model.Notification;
import com.example.poetvine.server.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {

    Optional<Notification> findById(long id);

    Set<Notification> findAllByRecipient(User user);
}
