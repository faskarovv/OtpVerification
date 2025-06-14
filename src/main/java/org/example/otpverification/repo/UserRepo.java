package org.example.otpverification.repo;

import org.example.otpverification.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Card, UUID> {

    Optional<Card> findByEmail(String email);
}
