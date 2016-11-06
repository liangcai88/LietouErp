package com.ccai.lietouerp;

import org.apache.shiro.SecurityUtils;

import com.ccai.lietouerp.db.entity.ErpUser;

public class AppUtils {

	/**
	 * 获取session用户信息
	 * @return
	 */
	public static ErpUser getSessionUser(){
		return (ErpUser)SecurityUtils.getSubject().getSession().getAttribute("sessionUser");
	}
}
