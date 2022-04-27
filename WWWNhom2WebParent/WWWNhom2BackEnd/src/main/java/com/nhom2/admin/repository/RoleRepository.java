package com.nhom2.admin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nhom2.common.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
