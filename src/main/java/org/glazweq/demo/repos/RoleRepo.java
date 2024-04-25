package org.glazweq.demo.repos;


import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Role findByName(String name);

}