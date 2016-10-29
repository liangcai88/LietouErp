var Login = function () {
    return {
        init: function () {
        	
        	$.validator.addMethod("mobile",function(value,elemnet,params){ 
       		  var partten = /^0?1(3|5|8|7|4)\d{9}$/; 
       			if(partten.test(value)){  
       				return true ;
       			}else{
       				return false ;
       			}
       	  });
        	
           $('.login-form').validate({
	            errorElement: 'label', 
	            errorClass: 'help-inline',
	            focusInvalid: false, 
	            rules: {
	                username: {
	                    required: true
	                },
	                password: {
	                    required: true
	                },
	                remember: {
	                    required: false
	                }
	            },

	            messages: {
	                username: {
	                    required: "账号不能为空."
	                },
	                password: {
	                    required: "密码不能为空."
	                }
	            },

	            invalidHandler: function (event, validator) {     
	            },

	            highlight: function (element) { 
	                $(element)
	                    .closest('.control-group').addClass('error'); 
	            },

	            success: function (label) {
	                label.closest('.control-group').removeClass('error');
	                label.remove();
	            },

	            errorPlacement: function (error, element) {
	                error.addClass('help-small no-left-padding').insertAfter(element.closest('.input-icon'));
	            },

	            submitHandler: function (form) { 
	            	 $('.alert-error', $('.login-form')).hide();
	            	 $("#login-submit-btn").button('loading');
		            	$(form).ajaxSubmit(function(data){  
		            		if(data && data.success){ 
		            			window.location.href=data.gourl;   
		            		}else if(data && !data.success){    
		            			$('.alert-error', $('.login-form')).find("span").html(data.msg);
		            	        $('.alert-error', $('.login-form')).show();
		            	        $("#login-submit-btn").button('reset');  
		            		}
		            		return false;  
		            	});
		            return false;
	            }
	        });

//	        $('.login-form input').keypress(function (e) {
//	            if (e.which == 13) {
//	                if ($('.login-form').validate().form()) {
//	                    window.location.href = "index.html";
//	                }
//	                return false;
//	            }  
//	        });

	        $('.forget-form').validate({
	            errorElement: 'label',
	            errorClass: 'help-inline',
	            focusInvalid: false,
	            ignore: "",
	            rules: {
	                email: {
	                    required: true,
	                    email: true
	                }
	            },

	            messages: {
	                mobile: {
	                    required: "手机号码不能为空."
	                }
	            },

	            invalidHandler: function (event, validator) {   

	            },

	            highlight: function (element) {
	                $(element)
	                    .closest('.control-group').addClass('error'); 
	            },

	            success: function (label) {
	                label.closest('.control-group').removeClass('error');
	                label.remove();
	            },

	            errorPlacement: function (error, element) {
	                error.addClass('help-small no-left-padding').insertAfter(element.closest('.input-icon'));
	            },

	            submitHandler: function (form) {
	                window.location.href = "index.html";
	            }
	        });

//	        $('.forget-form input').keypress(function (e) {
//	            if (e.which == 13) {
//	                if ($('.forget-form').validate().form()) {
//	                    window.location.href = "index.html";
//	                }
//	                return false;
//	            }
//	        });

	        jQuery('#forget-password').click(function () {
	        	History.pushState(null, "找回密码", $(this).attr("href")); 
	            jQuery('.login-form').hide();
	            jQuery('.forget-form').show();
	            return false;
	        });

	        jQuery('#back-btn').click(function () {
	        	History.pushState(null, "账号登录", $(this).attr("href")); 
	            jQuery('.login-form').show();
	            jQuery('.forget-form').hide();
	            return false;
	        });

	        $('.register-form').validate({
	            errorElement: 'label',
	            errorClass: 'help-inline',
	            focusInvalid: false,
	            ignore: "",
	            rules: {
	            	mobile: {
	                    required: true,
	                    mobile:true
	                },
	                password: {
	                    required: true,
	                    maxlength: 30
	                },
	                rpassword: {
	                    equalTo: "#register_password"
	                },
	                trueName: {
	                    required: true,
	                    maxlength: 50
	                },
	                kaptcha:{
	                	 required: true,
	                	 maxlength: 5
	                }
	            },

	            messages: {
	            	mobile: {
	                    required: "手机号码不能为空",
	                    mobile:"手机号码格式错误"
	                },
	                password: {
	                    required: "密码不能为空",
	                    maxlength: "长度超出正常范围"
	                },
	                rpassword: {
	                	equalTo: "确认密码不一致"
	                }, 
	                trueName: {
	                    required: "姓名不能为空",
	                    maxlength: "长度超出正常范围",
	                },
	                kaptcha:{ 
	                	 required: "验证码不能为空",
	                	 maxlength: "长度超出正常范围"
	                }
	            },

	            invalidHandler: function (event, validator) {

	            },

	            highlight: function (element) {
	                $(element)
	                    .closest('.control-group').addClass('error');
	            },

	            success: function (label) {
	                label.closest('.control-group').removeClass('error');
	                label.remove();
	            },

	            errorPlacement: function (error, element) {
	                if (element.attr("name") == "tnc") {
	                    error.addClass('help-small no-left-padding').insertAfter($('#register_tnc_error'));
	                } else {
	                    error.addClass('help-small no-left-padding').insertAfter(element.closest('.input-icon'));
	                }
	            },

	            submitHandler: function (form) { 
	                $('.alert-error', $('.register-form')).hide();
	            	 $("#register-submit-btn").button('loading');
		            	$(form).ajaxSubmit(function(data){  
		            		if(data && data.success){ 
		            			window.location.href=data.gourl;   
		            		}else{    
		            			$('.alert-error', $('.register-form')).find("span").html(data.msg);
		            	        $('.alert-error', $('.register-form')).show();
		            	        $("#register-submit-btn").button('reset');  
		            		} 
		            		return false;  
		            	});
		            return false;
	            }
	        });

	        jQuery('#register-btn').click(function () {
	        	History.pushState(null, "账号注册", $(this).attr("href")); 
	            jQuery('.login-form').hide();
	            jQuery('.register-form').show();
	            return false;
	        });

	        jQuery('#register-back-btn').click(function () { 
	        	History.pushState(null, "账号登录", $(this).attr("href")); 
	            jQuery('.login-form').show();
	            jQuery('.register-form').hide();
	            return false;
	        });
        }

    };

}();