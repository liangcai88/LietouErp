package com.ccai.lietouerp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import com.ccai.lietouerp.db.entity.ResumeInfo;

/**
 * 简历接口
 * @author Administrator
 *
 */
public interface ResumeInfoService {
	
	public ResumeInfo findById(Long id);

	public ResumeInfo save(ResumeInfo resumeInfo);
	
	public ResumeInfo findByMobile(String mobile);
	
	public Page<ResumeInfo> search(Specification<ResumeInfo> spec,Integer pageNumber,Integer pageSize);
	
	
	public void deleteResumeWorkExperience(Long ... ids);
}
