package com.ccai.lietouerp.auth;

import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


public class ErpRetryHashedMatcher extends HashedCredentialsMatcher {

	
	@Resource
	protected Environment env;
	
	@Autowired
	private EhCacheManager ehCacheManager;

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		 String username = (String) token.getPrincipal();
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		HttpSession session = request.getSession();
		String imgvalid = env.getProperty("erp.login.imgvalid", "false");
		if (imgvalid.equals("true")) {
			String kaptchacode = request.getParameter("kaptchacode");
			String sessionKaptchacode = (String) session.getAttribute(env.getProperty("kaptcha.session.key", "sessionKaptchacode"));
			if (StringUtils.isEmpty(kaptchacode) || StringUtils.isEmpty(sessionKaptchacode)
					|| !kaptchacode.equalsIgnoreCase(sessionKaptchacode)) {
				throw new KaptchaValidateFailedException();
//				return false;
			}
		}
		Cache<String, AtomicInteger>  passwordRetryCache= ehCacheManager.getCache("passwordRetryCache");
		 AtomicInteger retryCount = passwordRetryCache.get(username); 
		if (retryCount == null) {  
            retryCount = new AtomicInteger(0);  
            passwordRetryCache.put(username, retryCount);  
        }  
        if (retryCount.incrementAndGet() > 10) {  
            throw new ExcessiveAttemptsException();  
        }  
		boolean matches = super.doCredentialsMatch(token, info);
		if(matches){
			passwordRetryCache.remove(username);
		}
		return matches;
	}

	@Override
	public Hash hashProvidedCredentials(Object credentials, Object salt, int hashIterations) {
		return super.hashProvidedCredentials(credentials, salt, hashIterations);
	}
	
	@Override
	public boolean equals(Object tokenCredentials, Object accountCredentials) {
		return super.equals(tokenCredentials, accountCredentials);
	}
	
	
}
