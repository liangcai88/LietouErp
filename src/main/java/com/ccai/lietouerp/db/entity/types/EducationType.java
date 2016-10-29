package com.ccai.lietouerp.db.entity.types;

public enum EducationType {
	
	E0("中专或相当学历"),
	E1("大专"),
	E2("本科"),
	E3("双学士"),
	E4("硕士"),
	E5("博士"),
	E6("博士后");

	private String info;
	

	EducationType(String info){
		this.info=info;
	}
	
	public String getInfo(){
		return info;
	}
}
