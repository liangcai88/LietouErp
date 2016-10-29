package com.ccai.lietouerp.configs.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ccai.lietouerp.auth.ErpRetryHashedMatcher;
import com.ccai.lietouerp.auth.ErpShiroRealm;
import com.ccai.lietouerp.web.filter.ErpFormAuthenticationFilter;

@Configuration
public class ShiroConfiguration {

	 /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，以为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     *
        Filter Chain定义说明
       1、一个URL可以配置多个Filter，使用逗号分隔
       2、当设置多个过滤器时，全部验证通过，才视为通过
       3、部分过滤器可指定参数，如perms，roles
     *
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(org.apache.shiro.mgt.SecurityManager securityManager){
       System.out.println("ShiroConfiguration.shirFilter()");
       ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
      
        // 必须设置 SecurityManager 
       shiroFilterFactoryBean.setSecurityManager(securityManager);
       
       
       //拦截器.
       Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
      
       //配置退出过滤器,其中的具体的退出代码Shiro已经替我们实现了 
       filterChainDefinitionMap.put("/css/**", "anon");
       filterChainDefinitionMap.put("/js/**", "anon");
       filterChainDefinitionMap.put("/image/**", "anon");
       filterChainDefinitionMap.put("/vcode/**", "anon");
       filterChainDefinitionMap.put("/regist", "anon");
       filterChainDefinitionMap.put("/forget", "anon");
       filterChainDefinitionMap.put("/touch", "anon");
       filterChainDefinitionMap.put("/logout", "logout");
       filterChainDefinitionMap.put("/login", "authc");
       filterChainDefinitionMap.put("/hello", "anon");
      
       //<!-- 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边 -->:这是一个坑呢，一不小心代码就不好使了;
        //<!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问-->
       filterChainDefinitionMap.put("/**", "user");
      
       // 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/index");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
      
        
        
       shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
       
       shiroFilterFactoryBean.getFilters().put("authc", new ErpFormAuthenticationFilter());
       return shiroFilterFactoryBean;
    }
   
    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(){
       DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
       securityManager.setRealm(erpShiroRealm());
       //注入缓存管理器;
       securityManager.setCacheManager(ehCacheManager());//这个如果执行多次，也是同样的一个对
       securityManager.setRememberMeManager(rememberMeManager());
       return securityManager;
    }
    
    
    /**
     * 身份认证realm;
     * (这个需要自己写，账号密码校验；权限等)
     * @return
     */
    @Bean
    public ErpShiroRealm erpShiroRealm(){
    	ErpShiroRealm erpShiroRealm = new ErpShiroRealm();
    	erpShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
       return erpShiroRealm;
    }
    
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
    	ErpRetryHashedMatcher hashedCredentialsMatcher = new ErpRetryHashedMatcher();
      
       hashedCredentialsMatcher.setHashAlgorithmName("sha1");//散列算法:这里使用sha1算法;
       hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 sha1(sha1(""));
       return hashedCredentialsMatcher;
    }
    
    /**
     *  开启shiro aop注解支持.
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(org.apache.shiro.mgt.SecurityManager securityManager){
       AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
       authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
       return authorizationAttributeSourceAdvisor;
    }
    
    @Bean
    public SimpleCookie rememberMeCookie(){
       //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
       SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
       //<!-- 记住我cookie生效时间session ,单位秒;-->
       simpleCookie.setMaxAge(-1);
       return simpleCookie;
    }
   
    /**
     * cookie管理对象;
     * @return
     */
    @Bean
    public CookieRememberMeManager rememberMeManager(){
       CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
       cookieRememberMeManager.setCookie(rememberMeCookie());
       return cookieRememberMeManager;
    }
    
    /**
     * shiro缓存管理器;
     * 需要注入对应的其它的实体类中：
     * 1、安全管理器：securityManager
     * 可见securityManager是整个shiro的核心；
     * @return
     */
    @Bean
    public EhCacheManager ehCacheManager(){
       EhCacheManager cacheManager = new EhCacheManager();
       cacheManager.setCacheManagerConfigFile("classpath:config/ehcache-shiro.xml");
       return cacheManager;
    }
   
}
