package org.glazweq.demo.repos;


import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByEmail(String email);
    User findByUsername(String username);

    List<User> findUsersByEmail(String email);
    User findUserById(Long id);
    List<User> findUsersByUsername(String username);
    List<User> findUsersByRoles(List<Role> roles);
    List<User> findUsersByName(String name);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:partialName%")
    List<User> findByPartialName(@Param("partialName") String partialName);
    @Query("SELECT u FROM User u WHERE u.email LIKE %:partialMail%")
    List<User> findByPartialMail(@Param("partialMail") String partialMail);
    @Query("SELECT u FROM User u WHERE u.username LIKE %:partialUsername%")
    List<User> findByPartialUsername(@Param("partialUsername") String partialUsername);


}
