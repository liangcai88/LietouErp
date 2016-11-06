package com.ccai.lietouerp.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ccai.lietouerp.db.dao.ResumeInfoDao;
import com.ccai.lietouerp.db.entity.ResumeInfo;
import com.ccai.lietouerp.services.ResumeInfoService;


@Service
public class ResumeInfoServiceImpl implements ResumeInfoService {

	@Autowired
	private ResumeInfoDao resumeInfoDao;
	
	@Override
	public ResumeInfo save(ResumeInfo resumeInfo) {
		return resumeInfoDao.save(resumeInfo);
	}

	@Override
	public ResumeInfo findByMobile(String mobile) {
		return resumeInfoDao.findByMobile(mobile);
	}
	
    
	public Page<ResumeInfo> search(Specification<ResumeInfo> spec,Integer pageNumber,Integer pageSize){
		Sort sort = new Sort(Direction.ASC, "id");
		PageRequest pageRequest=new PageRequest(pageNumber - 1, pageSize, sort);
		return resumeInfoDao.findAll(spec,pageRequest);
	}

	@Override
	public ResumeInfo findById(Long id) {
		return resumeInfoDao.findOne(id);
	}

	

}
