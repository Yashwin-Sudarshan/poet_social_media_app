package com.example.poetvine.server.repository;

import com.example.poetvine.server.model.User;
import com.example.poetvine.server.model.enumeration.VisibilityPreference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Set<User> findAllByProfileVisibilityPreference(VisibilityPreference visibilityPreference);
}
