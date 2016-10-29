package com.ccai.lietouerp.services;

import com.ccai.lietouerp.db.entity.ErpUser;

/**
 * 用户相关接口
 * @author Administrator
 *
 */
public interface ErpUserService {

	/**
	 * 根据用户名获取用户信息
	 * @param userName
	 * @return
	 */
	public ErpUser findByUserName(String userName);
	
	
	/**
	 * 保存用户
	 * @param erpUser
	 * @return
	 */
	public ErpUser save(ErpUser erpUser);
}
