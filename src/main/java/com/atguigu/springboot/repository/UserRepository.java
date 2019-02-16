package com.atguigu.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.atguigu.springboot.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByName(String name);
}
