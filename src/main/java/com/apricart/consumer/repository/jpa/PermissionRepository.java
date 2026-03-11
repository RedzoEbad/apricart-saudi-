package com.apricart.consumer.repository.jpa;

import com.apricart.consumer.enity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PermissionRepository extends CrudRepository<Permission, Long> {

    List<Permission> findByActive(String activeStatus);
    Permission findByIdAndActive(Long id, String activeStatus);
    @Query(value = "select distinct category from permission order by category asc", nativeQuery = true)
    List<String> findDistinctCategory();
    boolean existsByApiURL(String apiURL);

}
