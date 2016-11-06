package com.ccai.lietouerp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ccai.lietouerp.AppUtils;
import com.ccai.lietouerp.db.entity.ResumeInfo;
import com.ccai.lietouerp.db.entity.ResumeWord;
import com.ccai.lietouerp.db.entity.ResumeWorkExperience;
import com.ccai.lietouerp.db.entity.types.EducationType;
import com.ccai.lietouerp.db.entity.types.ErpSexType;
import com.ccai.lietouerp.db.entity.types.MarriageType;
import com.ccai.lietouerp.services.ResumeInfoService;
import com.ccai.utils.Tools;
import com.ccai.utils.file.Word2Html;
import com.ccai.utils.pca.PCA;
import com.ccai.utils.pca.PCAUtils;

@RequestMapping("/resume")
@Controller
public class ResumeController {
	
	@Autowired
	private Word2Html word2Html;
	
	@Autowired
	private ResumeInfoService resumeInfoService;
	
	
	@RequestMapping(value = "/page/index")
	public String index(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model,
			@RequestParam(value = "pn", required = false) Integer pn,
			@RequestParam(value = "term", required = false) String term){
		if(pn==null || pn<1){
			pn=1;
		}
		model.put("educationTypes", EducationType.values());
    	model.put("privoces", PCAUtils.getInstance().findPrivoceList());
    	model.put("marriageTypes", MarriageType.values());
    	Specification<ResumeInfo> spec =null;
    	if(Tools.stringIsNotNull(term)){
    		spec = new Specification<ResumeInfo>() {
				@Override
				public Predicate toPredicate(Root<ResumeInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		              Predicate p3= cb.equal(root.join("workExperiences").get("company").as(String.class), term);
					   Predicate p1= cb.equal(root.get("mobile").as(String.class),term);
					   Predicate p2= cb.equal(root.get("trueName").as(String.class),term);
					   
					return cb.or(p1,p2,p3); 
				}
		};  
    	}
    	Page<ResumeInfo> pageData=resumeInfoService.search(spec, pn, 10);
    	if((pageData.getContent()==null || pageData.getContent().size()==0) && pageData.getTotalPages()>0){
    		 pn=1;
    		 pageData=resumeInfoService.search(spec, pn, 10);    
    	}
    	model.put("pageData", pageData);
        return "resume/indexresume";     
	}
	
	@RequestMapping(value = "/page/detail/{id}")
	public String detail(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model,
			@PathVariable("id") String id){
		model.put("educationTypes", EducationType.values());
    	model.put("privoces", PCAUtils.getInstance().findPrivoceList());
    	model.put("marriageTypes", MarriageType.values());
    	ResumeInfo info=resumeInfoService.findById(Tools.toLong(id));
    	model.put("info",info);
        return "resume/detailresume";     
	}
	
	@RequestMapping(value = "/page/add")
	public String add(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model){
		model.put("educationTypes", EducationType.values());
    	model.put("privoces", PCAUtils.getInstance().findPrivoceList());
    	model.put("marriageTypes", MarriageType.values());
        return "resume/addresume";     
	}

	
	@RequestMapping(value = "/page/edit/{id}")
	public String edit(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model,
			@PathVariable("id") String id){
		model.put("educationTypes", EducationType.values());
    	model.put("privoces", PCAUtils.getInstance().findPrivoceList());
    	model.put("marriageTypes", MarriageType.values());
    	ResumeInfo info=resumeInfoService.findById(Tools.toLong(id));
    	model.put("info",info);
        return "resume/addresume";     
	}
	

	/**
	 * 上传word简历
	 * 
	 * @param request
	 * @param response
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/worduploadtohtml")
	public @ResponseBody Object worduploadtohtml(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile file) {
		String userName=(String)SecurityUtils.getSubject().getPrincipal();
		Map<String,Object> resData=new HashMap<String,Object>();
		resData.put("success", false);
		resData.put("msg", "上传失败");
		OutputStream output=null;
		BufferedOutputStream bufferedOutput =null;
		File file2=null;
		try {
			 file2=new File("static/tmp/"+file.getOriginalFilename());
			output= new FileOutputStream(file2);
			bufferedOutput= new BufferedOutputStream(output);
			bufferedOutput.write(file.getBytes());
			String html=word2Html.convert2Html(file2,request.getContextPath(),userName);
			resData.put("success", true);
			resData.put("html", html);
			resData.put("msg", "上传成功");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(bufferedOutput!=null){
				try{
					bufferedOutput.close();
					output.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			} 
			if(file2!=null){
				file2.delete();
			}
		}
		return resData;
	}

	
	/**
	 * 添加简历
	 * @param request
	 * @param response
	 * @param trueName
	 * @return
	 */
	@RequestMapping(value = "/add")
	public @ResponseBody Object add(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "trueName", required = true) String trueName,
			@RequestParam(value = "sex", required = true) Integer sex,
			@RequestParam(value = "birthday", required = true) String birthday,
			@RequestParam(value = "marriage", required = false) String marriage,
			@RequestParam(value = "placeOrigin", required = false) Integer placeOrigin,
			@RequestParam(value = "localCity", required = false) Integer localCity,
			@RequestParam(value = "education", required = false) String education,
			@RequestParam(value = "gschool", required = false) String gschool,
			@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "weixin", required = false) String weixin,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "company", required = false) String[] company,
			@RequestParam(value = "jobTitle", required = false) String[] jobTitle,
			@RequestParam(value = "startTime", required = false) String[] startTime,
			@RequestParam(value = "endTime", required = false) String[] endTime,
			@RequestParam(value = "introduction", required = false) String introduction,
			@RequestParam(value = "wordhtml", required = false) String wordhtml) {
		Map<String,Object> res=new HashMap<String,Object>();
		res.put("success", false);
		if(Tools.stringIsNotNull(trueName,birthday,mobile)){
			ResumeInfo resumeInfo=new ResumeInfo();
			resumeInfo.setTrueName(trueName);
			if(!Tools.isMobileNum(mobile)){
				res.put("msg", "手机号码格式不正确");
				return res;
			}
			resumeInfo.setMobile(mobile);
			if(Tools.stringIsNotNull(email) && !Tools.isEmail(email)){
				res.put("msg", "邮箱格式不正确");
				return res;
			}
			resumeInfo.setEmail(email);
			Date birthDayDate=Tools.getDatefromString(birthday, "yyyy/MM/dd");
			if(birthDayDate==null){
				res.put("msg", "生日格式不正确");
				return res;
			}
			resumeInfo.setBirthday(birthDayDate);
			MarriageType marriageType=null;
			EducationType educationType=null;
			if(Tools.stringIsNotNull(marriage)){
				marriageType=Tools.getEnumValue(MarriageType.class, marriage);
				if(marriageType==null){
					res.put("msg", "婚姻状况选值不存在");
					return res;
				}
			}
			resumeInfo.setMarriage(marriageType);
			if(Tools.stringIsNotNull(education)){
				educationType=Tools.getEnumValue(EducationType.class, education);
				if(educationType==null){
					res.put("msg", "学历选值不存在");
					return res;
				}
			}
			resumeInfo.setEducation(educationType);
			PCA placeOriginPCA=null;
			PCA localCityPCA=null;
			if(placeOrigin!=null){
				placeOriginPCA=PCAUtils.getInstance().findById(placeOrigin.longValue());
				if(placeOriginPCA==null){
					res.put("msg", "籍贯选值不存在");
					return res;
				}
			}
			if(placeOriginPCA!=null)
			resumeInfo.setPlaceOrigin(placeOriginPCA.getName());
			if(localCity!=null){
				localCityPCA=PCAUtils.getInstance().findById(localCity.longValue());
				if(localCityPCA==null){
					res.put("msg", "所在地选值不存在");
					return res;
				}
			}
			if(localCityPCA!=null){
				resumeInfo.setLocalCity(localCityPCA.getName());
				resumeInfo.setLocalProvince(PCAUtils.getInstance().findById(localCityPCA.getParentId()).getName());
			}
			List<ResumeWorkExperience> workExperiences=new ArrayList<ResumeWorkExperience>();
			if(company!=null && jobTitle!=null && startTime!=null 
					&& endTime!=null && startTime.length>0 && startTime.length==endTime.length 
					&& startTime.length==jobTitle.length && jobTitle.length==company.length){
				for (int i = 0; i < company.length; i++) {
					ResumeWorkExperience wke=new ResumeWorkExperience();
					wke.setCompany(company[i]);
					wke.setJobTitle(jobTitle[i]);
					Date startDate=Tools.getDatefromString(startTime[i], "yyyy/MM");
					if(startDate==null){
						res.put("msg", "开始时间格式错误");
						return res;
					}
					Calendar cs=Calendar.getInstance();
					cs.setTime(startDate);
					Date endDate=Tools.getDatefromString(endTime[i], "yyyy/MM");
					if(endDate==null){
						res.put("msg", "结束时间格式错误");
						return res;
					}
					Calendar ce=Calendar.getInstance();
					ce.setTime(endDate);
					int startYear=cs.get(Calendar.YEAR);
					int startMonth=cs.get(Calendar.MONTH);
					int endYear=ce.get(Calendar.YEAR);
					int endMonth=ce.get(Calendar.MONTH);
					wke.setStartYear(startYear);
					wke.setStartMonth(startMonth);
					wke.setEndYear(endYear);
					wke.setEndMonth(endMonth);
					wke.setInfo(resumeInfo);
					workExperiences.add(wke);
					
				}
			}
			ResumeInfo resumeInfo1=resumeInfoService.findByMobile(mobile);
			if(resumeInfo1!=null){
				res.put("success", false);
				res.put("msg", "手机号码已经存在");
				return res;
			}
			resumeInfo.setWorkExperiences(workExperiences);
			resumeInfo.setIntroduction(introduction);
			if(Tools.stringIsNotNull(wordhtml)){
				ResumeWord word=new ResumeWord();
				word.setContent(wordhtml);
				word.setInfo(resumeInfo);
				resumeInfo.setWord(word);
			}
			resumeInfo.setCreateUid(AppUtils.getSessionUser().getId());
			resumeInfo.setInsertTime(new Date());
			resumeInfo.setUpdateTime(new Date());
			if(sex!=null){
				resumeInfo.setSex(Tools.getEnumValue(ErpSexType.class, sex));
			}
			resumeInfoService.save(resumeInfo);
			res.put("success", true);
			res.put("msg", "提交成功");
		}else{
			res.put("msg", "表单信息不完整");
		}
		return res;
	}
	
	/**
	 * 检查手机号码是否存在
	 * @param request
	 * @param response
	 * @param mobile
	 * @return
	 */
	@RequestMapping(value = "/checkmobile")
	public @ResponseBody Object checkmobile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "mobile", required = false) String mobile) {
		ResumeInfo resumeInfo1=resumeInfoService.findByMobile(mobile);
		if(resumeInfo1!=null){
			return false;
		}
		return true;
	}
	
//	public static void main(String[] args){
//		Date startDate=Tools.getDatefromString("2016/5", "yyyy/MM");
//		Calendar c=Calendar.getInstance();
//		c.setTime(startDate);
//		
//		System.out.println(c.get(Calendar.YEAR));
//	}
}
