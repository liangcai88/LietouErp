package com.ccai.lietouerp.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ccai.lietouerp.db.dao.ErpUserDao;
import com.ccai.lietouerp.db.entity.ErpUser;
import com.ccai.lietouerp.services.ErpUserService;

@Service
public class ErpUserServiceImpl implements ErpUserService {

	@Autowired
	private ErpUserDao erpUserDao;
	
	@Override
	public ErpUser findByUserName(String userName) {
		if(!StringUtils.isEmpty(userName)){
			return erpUserDao.findByUserName(userName);
		}
		return null;
	}

	@Override
	public ErpUser save(ErpUser erpUser) {
		return erpUserDao.save(erpUser);
	}

}
