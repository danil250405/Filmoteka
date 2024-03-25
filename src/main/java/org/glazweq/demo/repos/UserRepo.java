package org.glazweq.demo.repos;


import org.glazweq.demo.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);
}
