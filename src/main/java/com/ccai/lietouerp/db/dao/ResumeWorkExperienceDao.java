package com.ccai.lietouerp.db.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ccai.lietouerp.db.entity.ResumeWorkExperience;

@Repository
public interface ResumeWorkExperienceDao extends CrudRepository<ResumeWorkExperience,Long>{
	
}
