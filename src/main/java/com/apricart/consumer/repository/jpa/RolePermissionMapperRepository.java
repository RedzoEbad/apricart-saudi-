package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.RolePermissionMapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RolePermissionMapperRepository extends CrudRepository<RolePermissionMapper, Long> {

    @Query(value ="select * from role_permission_mapper where role_id = ?1", nativeQuery = true)
    List<RolePermissionMapper> findByRolesId(Long roleId);

    @Transactional
    @Modifying
   void deleteByRolesId(Long roleId);

}
