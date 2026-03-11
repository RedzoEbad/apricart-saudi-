package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Roles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Roles, Long> {

    List<Roles> findByActive(String activeStatus);
    Roles findByIdAndActive(Long id, String activeStatus);

}
