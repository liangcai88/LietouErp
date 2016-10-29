package com.ccai.lietouerp.db.entity.types;

public enum ErpSexType {

	Male("男"),
	Female("女");
	
	private final String info;

	private ErpSexType(String info)
	{
		this.info = info;
	}

	public String getInfo()
	{
		return info;
	}
	
	public int getIndex(){
		return this.ordinal();
	}
}
