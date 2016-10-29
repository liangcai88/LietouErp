package com.ccai.lietouerp.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWord {

    @RequestMapping("/hello")
    public String hello(Map<String, Object> model){  
    	model.put("hello", "hello word");
        return "hello";     
    }  
    
	
   
}
 