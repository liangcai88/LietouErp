package com.ccai.lietouerp.auth;

import java.util.List;

/**
 * 角色权限配置对象
 * @author Administrator
 *
 */
public class ErpRolePermissions {

	private String role;
	
	private List<String> permissions;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	
	
	
	
}
