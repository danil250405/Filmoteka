package org.glazweq.demo.repos;


import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);

    List<User> findUsersByEmail(String email);
    User findUsersById(Long id);
    List<User> findUsersByUsername(String username);
    List<User> findUsersByRoles(List<Role> roles);
    List<User> findUsersByName(String name);



}
