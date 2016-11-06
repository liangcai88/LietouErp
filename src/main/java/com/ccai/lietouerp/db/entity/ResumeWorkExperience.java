package com.ccai.lietouerp.db.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;

/**
 * 工作经历
 * @author Administrator
 *
 */
@Entity
@Table(name="ResumeWorkExperience")
public class ResumeWorkExperience implements Serializable{

	private static final long serialVersionUID = 7465489869243398969L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Field(analyze=Analyze.YES)
	@Column(length=50)
	private String company;//工作单位
	
	@Column(length=5)
	private  Integer startYear;
	
	@Column(length=5)
	private Integer startMonth;
	
	@Column(length=5)
	private  Integer endYear;//小于等于零为 至今
	
	@Column(length=5)
	private Integer endMonth;
	
	@Field(analyze=Analyze.YES)
	@Column(length=100)
	private String jobTitle;//职位名称
	
	@Column(length=2000)
	private String detail;//详情描述
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	@JoinColumn(name="rid")
	@ContainedIn//这个注解的作用是告诉Lucene，在word实体类型的索引中需要同时包含对应info的数据。
	private ResumeInfo info;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public Integer getStartYear() {
		return startYear;
	}

	public void setStartYear(Integer startYear) {
		this.startYear = startYear;
	}

	public Integer getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(Integer startMonth) {
		this.startMonth = startMonth;
	}

	public Integer getEndYear() {
		return endYear;
	}

	public void setEndYear(Integer endYear) {
		this.endYear = endYear;
	}

	public Integer getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(Integer endMonth) {
		this.endMonth = endMonth;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public ResumeInfo getInfo() {
		return info;
	}

	public void setInfo(ResumeInfo info) {
		this.info = info;
	}
	
	
	public Date getStartDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy/MM");
		try {
			return format.parse(this.getStartYear()+"/"+this.getStartMonth());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public Date getEndDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy/MM");
		try {
			return format.parse(this.getEndYear()+"/"+this.getEndMonth());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
