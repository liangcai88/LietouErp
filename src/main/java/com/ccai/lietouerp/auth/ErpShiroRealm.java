package com.ccai.lietouerp.auth;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ccai.lietouerp.configs.shiro.ErpRolePermissionsConfigs;
import com.ccai.lietouerp.db.entity.ErpUser;
import com.ccai.lietouerp.db.entity.types.ErpUserStatus;
import com.ccai.lietouerp.services.ErpUserService;

/**
 * 用户认证和授权
 * @author Administrator
 *
 */
public class ErpShiroRealm extends AuthorizingRealm{
	
	@Autowired
	private EhCacheManager ehCacheManager;
	
	@Autowired
	private  ErpUserService erpUserService;
	
	@Autowired
	private ErpRolePermissionsConfigs erpRolePermissionsConfigs;

	/**
	 * 用户授权
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String userName=(String)principals.getPrimaryPrincipal();
		
		ErpUser erpUser=erpUserService.findByUserName(userName);
		if(erpUser!=null && erpUser.getStatus()!=null && erpUser.getStatus().ordinal()==ErpUserStatus.Valid.ordinal()){
			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
			authorizationInfo.addRole(erpUser.getRole().name());
			ErpRolePermissions erpRolePermissions=erpRolePermissionsConfigs.getErpRolePermissions(erpUser.getRole().name());
			if(erpRolePermissions!=null)
			authorizationInfo.addStringPermissions(erpRolePermissions.getPermissions());
			return authorizationInfo;
		}
		return null;
	}

	/**
	 * 用户认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String userName=(String)token.getPrincipal();
		if(!StringUtils.isEmpty(userName) ){
			ErpUser erpUser=erpUserService.findByUserName(userName);
			String erppwd="***"+userName+System.currentTimeMillis()+Math.random();
			if(erpUser!=null && erpUser.getStatus()!=null && erpUser.getStatus().ordinal()==ErpUserStatus.Valid.ordinal()){
				 erppwd=erpUser.getPassword();
			}else{
				erppwd=getHashedPassword(userName, erppwd);
			}
			SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
					userName, //用户名
					erppwd, //密码
		                ByteSource.Util.bytes(getCredentialsSalt(userName)),
		                getName()  //realm name
			);
			return authenticationInfo;
		}
		
		return null;
	}

	/**
	 * 获取hashed密码
	 * @param userName
	 * @param password
	 * @return
	 */
	public String  getHashedPassword(String userName,String password){
		ErpRetryHashedMatcher erpRetryHashedMatcher=(ErpRetryHashedMatcher)this.getCredentialsMatcher();
		String hex=erpRetryHashedMatcher.hashProvidedCredentials(password, getCredentialsSalt(userName), 2).toHex();
		return hex;
	}
	
	
	/**
	 * 获取登录失败次数
	 * @return
	 */
	public Integer getRetryCount(String username){
		if(username==null)
			return 0; 
		Cache<String, AtomicInteger>  passwordRetryCache= ehCacheManager.getCache("passwordRetryCache");
		 AtomicInteger retryCount = passwordRetryCache.get(username); 
		 if (retryCount == null) {  
	            return 0;
	     }
		 return retryCount.get();
	}
	
//	private String getSha1Pwd(String userName,String password){
//		String secret="1011leiyouero)(2016*+_88";
//		String str=userName+secret+password+secret;
//		return 	DigestUtils.sha1Hex(str);
//	}
	
	private String getCredentialsSalt(String userName){
		String salt="1011leiyouero)(2016*+_88";
		return userName+salt+userName;
	}
}
