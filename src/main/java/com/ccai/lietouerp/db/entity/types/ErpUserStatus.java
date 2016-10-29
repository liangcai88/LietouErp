package com.ccai.lietouerp.db.entity.types;

public enum ErpUserStatus {

	
	Demand("申请")
	, Valid("正常")
	, Invalid("无效")
	, Lock("锁定")
	, BlackList("黑名单")
	, Delete("删除");
	private String info;
	

	ErpUserStatus(String info){
		this.info=info;
	}
	
	public String getInfo(){
		return info;
	}
}
