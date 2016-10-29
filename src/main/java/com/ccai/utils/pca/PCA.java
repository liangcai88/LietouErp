package com.ccai.utils.pca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 地区
 * @author caicai
 *
 */
public class PCA  implements Serializable{

	private static final long serialVersionUID = 6315097988626904561L;
	
	private Long id;
	
	private Integer level;//0、省份 2、城市 3、地区
	
	private String name;
	
	private Long parentId;
	
	private String pinyin;
	
	private String pname;
	
	private List<PCA> childs=new ArrayList<PCA>();
	
	public PCA(){}

	public PCA(Long id,Integer level,String name,Long parentId){
		this.id=id;
		this.level=level;
		this.name=name;
		this.parentId=parentId;
	}
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	/**
	 * @return the pname
	 */
	public String getPname() {
		return pname;
	}

	/**
	 * @param pname the pname to set
	 */
	public void setPname(String pname) {
		this.pname = pname;
	}

	public List<PCA> getChilds() {
		return childs;
	}

	public void setChilds(List<PCA> childs) {
		this.childs = childs;
	}
	
	

}
