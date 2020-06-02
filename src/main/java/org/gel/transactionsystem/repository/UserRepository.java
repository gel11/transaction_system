package org.gel.transactionsystem.repository;

import org.gel.transactionsystem.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
