package com.ccai.lietouerp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ccai.lietouerp.auth.ErpShiroRealm;
import com.ccai.lietouerp.auth.KaptchaValidateFailedException;
import com.ccai.lietouerp.db.entity.ErpUser;
import com.ccai.lietouerp.db.entity.types.ErpUserRole;
import com.ccai.lietouerp.db.entity.types.ErpUserStatus;
import com.ccai.lietouerp.services.ErpUserService;
import com.ccai.utils.Tools;

@Controller
public class AccountController {

	@Autowired
	private ErpShiroRealm erpShiroRealm;

	@Autowired
	private ErpUserService erpUserService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, Model model) {
		model.addAttribute("curpage", "login");
		if(SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().isRemembered())
			return "redirect:"+request.getContextPath()+"/index";
		return "login";
	}
	

	/**
	 * 登录
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody Object login(HttpServletRequest request) {
		// 登录失败从request中获取shiro处理的异常信息。
		// shiroLoginFailure:就是shiro异常类的全类名.
		String exception = (String) request.getAttribute("shiroLoginFailure");
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", false);
		String msg = "";
		if (exception != null) {
			String username=request.getParameter("username");
			int retryCount=erpShiroRealm.getRetryCount(username);
			if (UnknownAccountException.class.getName().equals(exception)) {
				msg = "账号或密码不正确！"; 
				if(retryCount>2){
					msg +="你已经登录失败"+retryCount+"次，还可以尝试"+(10-retryCount)+"次";
				}
			} else if (IncorrectCredentialsException.class.getName().equals(exception)) {
				msg = "账号或密码不正确！"; 
				if(retryCount>2){
					msg +="你已经登录失败"+retryCount+"次，还可以尝试"+(10-retryCount)+"次";
				}
			} else if (KaptchaValidateFailedException.class.getName().equals(exception)) {
				msg = "验证码错误"; 
			}else if (ExcessiveAttemptsException.class.getName().equals(exception)) {
				msg = "你的账号已经被冻结，请10分钟之后再来尝试"; 
			}  else { 
				msg = "" + exception;
			}
			res.put("msg", msg);
		}else{
			String username=request.getParameter("username");
			ErpUser erpuser=erpUserService.findByUserName(username);
			request.getSession().setAttribute("sessionUser", erpuser);
			res.put("success", true);
			res.put("gourl", request.getContextPath() + "/index");
		}
		return res;
	}

	/**
	 * 注册
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.GET)
	public String regist(HttpServletRequest request, Model model) {
		if(SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().isRemembered())
			return "redirect:"+request.getContextPath()+"/index";
		model.addAttribute("curpage", "regist");
		return "login";
	}

	/**
	 * 注册
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/regist", method = RequestMethod.POST)
	public @ResponseBody Object regist(HttpServletRequest request,
			@RequestParam(value = "mobile", required = true) String mobile,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "trueName", required = true) String trueName) {
		Map<String, Object> res = new HashMap<String, Object>();
		res.put("success", false);
		if (Tools.stringIsNotNull(mobile, password, trueName) && Tools.isMobileNum(mobile)) {
			ErpUser erpUser = erpUserService.findByUserName(mobile);
			if (erpUser == null) {
				String hashedPwd = erpShiroRealm.getHashedPassword(mobile, password);
				erpUser = new ErpUser();
				erpUser.setUserName(mobile);
				erpUser.setMobile(mobile);
				erpUser.setPassword(hashedPwd);
				erpUser.setTrueName(trueName);
				erpUser.setNickName(trueName);
				erpUser.setRole(ErpUserRole.NULL);
				erpUser.setStatus(ErpUserStatus.Valid);
				erpUser.setInsertTime(new Date());
				erpUser.setUpdateTime(new Date());
				ErpUser dbErpUser = erpUserService.save(erpUser); 
				try {
					SecurityUtils.getSubject().login(new UsernamePasswordToken(dbErpUser.getUserName(), password,true));
					res.put("success", true);
					res.put("gourl", request.getContextPath() + "/index");
				} catch (AuthenticationException e) {
					e.printStackTrace();
				}
			} else {
				res.put("msg", "手机号码已经被注册");
			}
		} else {
			res.put("msg", "注册信息不完整");
			if (!Tools.isMobileNum(mobile))
				res.put("msg", "手机号码格式不正确");
		}

		return res;
	}

	/**
	 * 找回密码
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/forget")
	public String forget(HttpServletRequest request, Model model) {
		if(SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().isRemembered())
			return "redirect:"+request.getContextPath()+"/index";
		model.addAttribute("curpage", "forget");
		String method = request.getMethod();
		if (method.equalsIgnoreCase("GET")) {
			return "login";
		}
		return "login";
	}
	
	
	/**
	 * 登录后ajax定时刷新会话
	 * @return
	 */
	@RequestMapping(value = "/touch", method = RequestMethod.GET)
	public @ResponseBody Object touch() {
		return 1;
	}
}
