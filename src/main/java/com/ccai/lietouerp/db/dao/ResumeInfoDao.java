package com.ccai.lietouerp.db.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ccai.lietouerp.db.entity.ErpUser;
import com.ccai.lietouerp.db.entity.ResumeInfo;

@Repository
public interface ResumeInfoDao extends PagingAndSortingRepository<ResumeInfo,Long>,JpaSpecificationExecutor<ResumeInfo>{

	public ResumeInfo findByMobile(String mobile);
	
}
