package com.atguigu.springboot.service;

import java.util.Map;

import com.atguigu.springboot.domain.Role;
import com.atguigu.springboot.domain.User;

public interface ILoginService {
	/**
	 * 添加用户
	 * @param map
	 * @return
	 */
	public User addUser(Map<String,Object> map);
	/**
	 * 添加角色
	 * @param map
	 * @return
	 */
	public Role addRole(Map<String,Object> map);
	/**
	 * 查询用户通过用户名
	 * @param name
	 * @return
	 */
	public User findByName(String name);
}
