package org.glazweq.demo.repos;

import org.glazweq.demo.domain.User;
import org.glazweq.demo.domain.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserBanRepo extends JpaRepository<UserBan, Long> {
    UserBan findUserBanByUser(User user);
}
