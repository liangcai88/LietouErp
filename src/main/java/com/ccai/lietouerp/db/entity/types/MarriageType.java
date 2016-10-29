package com.ccai.lietouerp.db.entity.types;

/**
 * 婚姻状况
 * @author Administrator
 *
 */
public enum MarriageType {

	Single("单身"),
	Married("已婚");
	
	private final String info;

	private MarriageType(String info)
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
