package com.ccai.utils.pca;

import java.io.Serializable;
import java.util.List;

public class FirstLetterCityList implements Serializable{

	private static final long serialVersionUID = 8796804991594547206L;
	
	private String letter;//首字母
	
	private List<PCA> list;

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public List<PCA> getList() {
		return list;
	}

	public void setList(List<PCA> list) {
		this.list = list;
	}
	
	
}
