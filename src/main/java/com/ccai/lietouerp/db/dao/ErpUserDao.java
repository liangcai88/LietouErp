package com.ccai.lietouerp.db.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ccai.lietouerp.db.entity.ErpUser;


@Repository
public interface ErpUserDao extends PagingAndSortingRepository<ErpUser,Long>{

	@Query("select u from ErpUser u where u.userName=:un")
	public ErpUser findByUserName(@Param("un") String userName);
	
}
