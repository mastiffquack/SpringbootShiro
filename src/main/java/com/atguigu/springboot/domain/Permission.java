package com.atguigu.springboot.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="permission")
public class Permission {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)//主键自动
	private Long id;
	private String permission;
	@ManyToOne(fetch = FetchType.EAGER)//优先加载
	private Role role;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "Permission{" +
				"id=" + id +
				", permission='" + permission + '\'' +
				", role=" + role +
				'}';
	}
}
