package com.ccai.lietouerp.db.entity.types;

/**
 * 用户角色
 * @author Administrator
 *
 */
public enum ErpUserRole {
	NULL("无角色"),
	SuperAdmin("超级管理员"),
	Admin("管理员"),
	CustomerService("客服"),
	Adviser("顾问"),
	PartTimeWorker("兼职人员");
	
	private String info;
	

	ErpUserRole(String info){
		this.info=info;
	}
	
	public String getInfo(){
		return info;
	}
	
}
