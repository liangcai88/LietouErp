package com.ccai.lietouerp.configs.mvc;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.velocity.VelocityLayoutViewResolver;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import com.ccai.utils.Tools;
import com.ccai.utils.file.Word2Html;
import com.google.code.kaptcha.servlet.KaptchaServlet;

@Configuration  
public class WebMvcConfig  extends WebMvcConfigurerAdapter {

	 /*@Value("${kaptcha.border}") 
    private String kborder;*/  
      
    @Value("${kaptcha.session.key}")  
    private String skey;  
      
    @Value("${kaptcha.textproducer.font.color}")  
    private String fcolor;  
      
    @Value("${kaptcha.textproducer.font.size}")  
    private String fsize;  
      
    @Value("${kaptcha.obscurificator.impl}")  
    private String obscurificator;  
      
    @Value("${kaptcha.noise.impl}")  
    private String noise;  
      
    @Value("${kaptcha.image.width}")  
    private String width;  
      
    @Value("${kaptcha.image.height}")  
    private String height;  
      
    @Value("${kaptcha.textproducer.char.length}")  
    private String clength;  
      
    @Value("${kaptcha.textproducer.char.space}")  
    private String cspace;  
      
    @Value("${kaptcha.background.clear.from}")  
    private String from;  
      
    @Value("${kaptcha.background.clear.to}")  
    private String to;  
    
    @Value("${web.app.static.path}")  
    private String staticPath;
    
    @Bean   
    public ServletRegistrationBean servletRegistrationBean() throws ServletException{ 
    	 ServletRegistrationBean servlet= new ServletRegistrationBean(new KaptchaServlet(),"/vcode/kaptcha.vjpg");  
    	 servlet.addInitParameter("kaptcha.border", "no"/*kborder*/);//无边框  
         servlet.addInitParameter("kaptcha.session.key", skey);//session key  
         servlet.addInitParameter("kaptcha.textproducer.font.color", fcolor);  
         servlet.addInitParameter("kaptcha.textproducer.font.size", fsize);  
         servlet.addInitParameter("kaptcha.obscurificator.impl", obscurificator);  
         servlet.addInitParameter("kaptcha.noise.impl", noise);  
         servlet.addInitParameter("kaptcha.image.width", width);  
         servlet.addInitParameter("kaptcha.image.height", height);  
         servlet.addInitParameter("kaptcha.textproducer.char.length", clength);  
         servlet.addInitParameter("kaptcha.textproducer.char.space", cspace);  
         servlet.addInitParameter("kaptcha.background.clear.from", from); //和登录框背景颜色一致   
         servlet.addInitParameter("kaptcha.background.clear.to", to);  
         return servlet;
    }  
    
    /**
     * 配置layout
     * @param properties
     * @return
     */
    @Bean
    public VelocityViewResolver velocityViewResolver(VelocityProperties properties) {
    VelocityLayoutViewResolver viewResolver = new VelocityLayoutViewResolver();
    viewResolver.setViewClass(VelocityLayoutToolboxView.class);
    viewResolver.setLayoutUrl("layout/empty.html");
    properties.applyToViewResolver(viewResolver);// 设置默认属性，比如前缀和后缀
    return viewResolver;
    }
    
    /**
     * 配置静态文件目录
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	if(Tools.stringIsNotNull(staticPath))
        registry.addResourceHandler("/**").addResourceLocations(staticPath);
        super.addResourceHandlers(registry);
    }
    
    @Bean
    public Word2Html word2Html(){
    	Word2Html word2Html=new Word2Html();
    	return word2Html;
    }
}
