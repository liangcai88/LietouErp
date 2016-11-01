package com.ccai.lietouerp.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ccai.utils.file.Word2Html;

@RequestMapping("/resume")
@Controller
public class ResumeController {
	
	@Autowired
	private Word2Html word2Html;

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
			 file2=new File(file.getOriginalFilename());
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
			if(file2!=null){
				file2.delete();
			}
			if(bufferedOutput!=null){
				try{
					bufferedOutput.close();
					output.close();
				}catch(Exception e){
					e.printStackTrace();
				}
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
			@RequestParam(value = "trueName", required = false) String trueName,
			@RequestParam(value = "sex", required = false) Integer sex,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "marriage", required = false) String marriage,
			@RequestParam(value = "placeOrigin", required = false) Integer placeOrigin,
			@RequestParam(value = "localCity", required = false) Integer localCity,
			@RequestParam(value = "education", required = false) Integer education,
			@RequestParam(value = "gschool", required = false) String gschool,
			@RequestParam(value = "mobile", required = false) String mobile,
			@RequestParam(value = "weixin", required = false) String weixin,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "company", required = true) String[] company,
			@RequestParam(value = "jobTitle", required = true) String[] jobTitle,
			@RequestParam(value = "startTime", required = true) String[] startTime,
			@RequestParam(value = "endTime", required = true) String[] endTime,
			@RequestParam(value = "introduction", required = true) String introduction,
			@RequestParam(value = "wordhtml", required = true) String wordhtml) {
		
		return null;
	}
}
