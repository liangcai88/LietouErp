package com.ccai.lietouerp.web;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ccai.lietouerp.db.entity.ErpUser;
import com.ccai.lietouerp.services.ErpUserService;

@Aspect
@Component
@Order(-5)
public class WebLogAspect {
	


	@Autowired
	private ErpUserService erpUserService;

	private Logger logger =  LoggerFactory.getLogger(this.getClass());
	   
    ThreadLocal<Long> startTime = new ThreadLocal<Long>();
   
    /**
     * 定义一个切入点.
     * 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
     @Pointcut("execution(public * com.ccai.lietouerp.controller..*.*(..))")
     public void webLog(){}
     
     public void doBefore(JoinPoint joinPoint){
        
//        
//      // 记录下请求内容
//        logger.info("URL : " + request.getRequestURL().toString());
//        logger.info("HTTP_METHOD : " + request.getMethod());
//        logger.info("IP : " + request.getRemoteAddr());
//        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
//        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));
//       //获取所有参数方法一：
//        Enumeration<String> enu=request.getParameterNames(); 
//        while(enu.hasMoreElements()){ 
//            String paraName=(String)enu.nextElement(); 
//            System.out.println(paraName+": "+request.getParameter(paraName)); 
//        } 
     }
     
     public void  doAfterReturning(JoinPoint joinPoint,Object retValue){
    	 Signature signature =  joinPoint.getSignature(); 
    	 Class<?> returnType = ((MethodSignature) signature).getReturnType(); 
    	 if(returnType==String.class){
    		 retValue="modules/"+retValue;
    	 }
    	 System.out.println(returnType);
       // 处理完请求，返回内容
//        logger.info("WebLogAspect.doAfterReturning()");
//        logger.info("execution time : " + (System.currentTimeMillis() - startTime.get()));
     }
     
     
     @Around("webLog()")
     public Object  around(ProceedingJoinPoint joinPoint) throws Throwable{
    	 startTime.set(System.currentTimeMillis());
    	 boolean isAjax=false;
         if(!SecurityUtils.getSubject().isAuthenticated() && SecurityUtils.getSubject().isRemembered()){
     	 	ErpUser erpUser= erpUserService.findByUserName((String)SecurityUtils.getSubject().getPrincipal());
     	 	SecurityUtils.getSubject().getSession().setAttribute("sessionUser", erpUser);

         }
//        // 接收到请求，记录请求内容
//         logger.info("WebLogAspect.doBefore()");
         ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
          HttpServletRequest request = attributes.getRequest();
          String requestType = request.getHeader("X-Requested-With"); 
          request.setAttribute("XMLHttpRequestType", "http");	
 		 if(requestType!=null &&  "XMLHttpRequest".equals(requestType)){
 			request.setAttribute("XMLHttpRequestType", "ajax");	
 			isAjax=true;
 		}
 		 
    	 Object resObj = joinPoint.proceed();  
    	 if(resObj!=null){
    		 Signature signature =  joinPoint.getSignature(); 
    		 MethodSignature methodSignature= ((MethodSignature) signature);
			ResponseBody responseBody=methodSignature.getMethod().getAnnotation(ResponseBody.class);
    		 if(responseBody==null){
    			 Class<?> returnType = methodSignature.getReturnType(); 
            	 if(isAjax && returnType==String.class && !resObj.toString().startsWith("redirect")){
            		 return "modules/"+resObj.toString();
            	 }else if(!isAjax && returnType==String.class && !resObj.toString().startsWith("redirect")){
            		 return "page/"+resObj.toString();
            	 }else{
            		 return resObj;
            	 }
    		 }
    	 }  
    	 return resObj;
    	
    	
     }
     
}
