package com.ccai.lietouerp.db.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

import com.ccai.utils.Tools;

/**
 * word简历
 * @author Administrator
 *
 */
@Entity
@Table(name="ResumeWord")
public class ResumeWord implements Serializable{
	
	private static final long serialVersionUID = -3525297214665429542L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(analyze=Analyze.YES)
	@Column(columnDefinition="Text",length=16777216) 
	private String content;
	
	@OneToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="rid")
	@ContainedIn//这个注解的作用是告诉Lucene，在word实体类型的索引中需要同时包含对应info的数据。
	private ResumeInfo info;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ResumeInfo getInfo() {
		return info;
	}

	public void setInfo(ResumeInfo info) {
		this.info = info;
	}
	
	public String getEncodeContent(){
		if(Tools.stringIsNotNull(this.getContent())){
			try {
				return URLEncoder.encode(this.getContent(), "utf8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	

}
