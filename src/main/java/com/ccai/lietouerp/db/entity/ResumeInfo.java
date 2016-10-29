package com.ccai.lietouerp.db.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import com.ccai.lietouerp.db.entity.types.EducationType;
import com.ccai.lietouerp.db.entity.types.ErpSexType;
import com.ccai.lietouerp.db.entity.types.MarriageType;

/**
 * 简历表
 * 使用hibernate search 做搜索，庖丁解牛分词器
 * 
 * @author Administrator
 *
 */
//ik-analyzer
@Indexed
@Analyzer(impl=ChineseAnalyzer.class)
@Entity
@Table(name="ResumeInfo")
public class ResumeInfo implements Serializable{

	private static final long serialVersionUID = 4272754802539710041L;
	
	
	@DocumentId
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertTime;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Field(analyze=Analyze.NO)
	@Column(length=30)
	private String trueName;//姓名
	
	@Field(analyze=Analyze.NO)
	@Column(length=100)
	private String englishName;
	
	@Field(analyze=Analyze.NO)
	@Enumerated(EnumType.ORDINAL)
	@Column(length=2)
	private ErpSexType sex;
	
	@Field(analyze=Analyze.NO)
	@Column(length=30)
	private String weixin;//微信号
	 
	@Field(analyze=Analyze.NO)
	@Column(length=20)
	private String mobile;//手机号码
	
	@Field(analyze=Analyze.NO)
	@Column(length=40)
	private String email;//邮箱
	
	@Field(analyze=Analyze.YES)
	@Column(length=60)
	private String company;//目前工作单位
	
	@Field(analyze=Analyze.NO)
	@Column(length=50)
	private String localProvince;
	@Field(analyze=Analyze.NO)
	@Column(length=50)
	private String localCity;
	@Field(analyze=Analyze.NO)
	@Column(length=50)
	private String localRegion;
	
	@Field(analyze=Analyze.NO)
	@Column(length=50)
	private String placeOrigin;//籍贯
	
	@Field(analyze=Analyze.NO)
	@Enumerated(EnumType.ORDINAL)
	private MarriageType marriage;//婚姻
	
	@Field(analyze=Analyze.NO)
	@Enumerated(EnumType.ORDINAL)
	private EducationType education;//学历
	
	@Field(analyze=Analyze.YES)
	@Column(length=100)
	private String gschool;//毕业学校
	
	@Field(analyze=Analyze.NO)
	private Date birthday;//出生年月日
	
	@Column(length=200)
	private String photo;//照片
	
	@Column(length=2)
	private Integer wordYears;//工作年限
	
	@Column(length=100)
	private String curJobTitle;//目前职位
	
	@Column(length=2000)
	private String introduction;//自我介绍
	
	@OneToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY,mappedBy="info")
	@IndexedEmbedded(depth=1)//个注解和@ContainedIn正好是一对，需要同时使用。它的目的是为了防止循环依赖
	private ResumeWord word;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY,mappedBy="info")
	private List<ResumeWorkExperience> workExperiences;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public ErpSexType getSex() {
		return sex;
	}

	public void setSex(ErpSexType sex) {
		this.sex = sex;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocalProvince() {
		return localProvince;
	}

	public void setLocalProvince(String localProvince) {
		this.localProvince = localProvince;
	}

	public String getLocalCity() {
		return localCity;
	}

	public void setLocalCity(String localCity) {
		this.localCity = localCity;
	}

	public String getLocalRegion() {
		return localRegion;
	}

	public void setLocalRegion(String localRegion) {
		this.localRegion = localRegion;
	}

	public EducationType getEducation() {
		return education;
	}

	public void setEducation(EducationType education) {
		this.education = education;
	}

	public String getGschool() {
		return gschool;
	}

	public void setGschool(String gschool) {
		this.gschool = gschool;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public ResumeWord getWord() {
		return word;
	}

	public void setWord(ResumeWord word) {
		this.word = word;
	}

	public List<ResumeWorkExperience> getWorkExperiences() {
		return workExperiences;
	}

	public void setWorkExperiences(List<ResumeWorkExperience> workExperiences) {
		this.workExperiences = workExperiences;
	}

	public Integer getWordYears() {
		return wordYears;
	}

	public void setWordYears(Integer wordYears) {
		this.wordYears = wordYears;
	} 
	
	
	
}
