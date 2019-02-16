package com.atguigu.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atguigu.springboot.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
