package com.ccai.lietouerp.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ccai.lietouerp.db.entity.types.EducationType;
import com.ccai.lietouerp.db.entity.types.MarriageType;
import com.ccai.lietouerp.services.ErpUserService;
import com.ccai.utils.pca.PCAUtils;

@Controller
public class IndexController {
	

	@Autowired
	private ErpUserService erpUserService;

	 @RequestMapping(value={"/index","/",""})
	 public String index(HttpServletRequest request,Map<String, Object> model){  
	    	model.put("hello", "hello word");
	    	model.put("educationTypes", EducationType.values());
	    	model.put("privoces", PCAUtils.getInstance().findPrivoceList());
	    	model.put("marriageTypes", MarriageType.values());
//	        return "resume/addresume"; 
	    	return "redirect:"+request.getContextPath()+"/resume/page/index";
	 } 
}
