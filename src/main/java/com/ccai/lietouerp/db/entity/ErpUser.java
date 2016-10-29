package com.ccai.lietouerp.db.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.ccai.lietouerp.db.entity.types.ErpUserRole;
import com.ccai.lietouerp.db.entity.types.ErpUserStatus;

/**
 * 用户表 
 * @author Administrator
 *
 */
@Entity
@Table(name="ErpUser",indexes={@Index(columnList="userName",unique=true)})
public class ErpUser implements Serializable{

	private static final long serialVersionUID = -6506568992098007704L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private ErpUserStatus status=ErpUserStatus.Demand;
	
	@NotEmpty
	@Column(length=50,unique=true)
	private String userName;
	
	@NotEmpty
	@Column(length=100)
	private String password;
	
	@Column(length=50)
	private String nickName;
	
	@Column(length=50)
	private String trueName;
	
	@Column(length=50)
	private String email;
	
	@Column(length=50)
	private String weixin;
	
	@Column(length=15,unique=true)
	private String mobile;
	
	@NotNull
	@Enumerated(EnumType.ORDINAL)
	private ErpUserRole role;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertTime;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;
	
	@Column(length=255)
	private String avatar;
	
	private Long introducer=0L;//介绍人
	
	private Long examinationer=0l;//审批人 

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public ErpUserRole getRole() {
		return role;
	}

	public void setRole(ErpUserRole role) {
		this.role = role;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public ErpUserStatus getStatus() {
		return status;
	}

	public void setStatus(ErpUserStatus status) {
		this.status = status;
	}

	public Long getIntroducer() {
		return introducer;
	}

	public void setIntroducer(Long introducer) {
		this.introducer = introducer;
	}

	public Long getExaminationer() {
		return examinationer;
	}

	public void setExaminationer(Long examinationer) {
		this.examinationer = examinationer;
	}
	
	
	
	
	
}
