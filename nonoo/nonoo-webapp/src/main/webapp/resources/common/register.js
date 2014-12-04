//
//	jQuery Validate example script
//
jQuery.validator.addMethod("alnum", function (value, element) {  
    return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);  
}, "只能包括英文字母和数字");  

jQuery.validator.addMethod("china", function (value, element) {  
	    var tel = /^[\u4e00-\u9fa5]+$/;  
	   return this.optional(element) || (tel.test(value));  
}, "请输入汉字");  

jQuery.validator.addMethod("string", function (value, element) {  
    return this.optional(element) || /^[\u0391-\uFFE5\w]+$/.test(value);  
}, "不允许包含特殊符号!"); 

jQuery.validator.addMethod("notEqualTo", function (value, element, param) {  
	    return value != $(param).val();  
}, $.validator.format("两次输入不能相同!"));   


$(document).ready(function(){
	  var $register = $("#register");
	  var $username = $("#username");
	  var $password = $("#password");
	  var $email = $("#email");
	  
	  $register.validate({
	    rules: {
	      email: {
		        required: true,
		        email: true,
		        remote:"register/check_email.jhtml"
		  }, 
	      username: {
	    	  required: true,
	    	  rangelength:[6,18],
	    	  alnum:true,
	    	  remote:"register/check_username.jhtml"
	      },
	      
	      name: {
	    	  required: true,
	    	  rangelength:[2,4],
	    	  china:true
	      },
	      
	      password: {  
		     required: true,  
		     maxlength: 32,  
		     minlength: 6
		   },
	      repassword: {  
	    	 required: true,  
	    	 equalTo: "#password"
	      }

	    },
	    messages: {
	      email:{
	    	 required: "输入的邮件地址不能为空！", 
	    	 email:"输入邮件地址的格式不正确",
	    	 remote: "该邮箱已被注册，请重新输入",
	      },
	      name:{
	    	 required: "真实姓名不能为空！",
	    	 rangelength:"真实姓名 只能由2-4个汉字组成",
	    	 china:"只能够输入中文"
	      },
	      username: {  
	       required: "用户名不能为空！",  
	       rangelength:"用户名长度只能在6-18位字符之间",
	       remote: "该用户名已被使用，请重新输入",
	       alnum: "用户名只能由字母和数字组成!"
	     },
	      password:{
		    required: "密码不能够为空",
		    minlength:"密码长度最少6位",
		    maxlength:"密码长度最多32位",
		  },
	      repassword:{
			    required: "请再次输入密码",
			    equalTo:"两次密码输入不一致,请重新输入",
		  }
	      
	    },
	    highlight : function(element) {  
	    	$(element).closest('.form-group div').addClass('has-error');  
	    },  
	    	  
	    success : function(label) {  
	    	label.closest('.form-group div').removeClass('has-error');  
	    	label.remove();  
	    },  
	    errorPlacement : function(error, element) {  
	       element.parent('div').append(error);  
	    },
	    submitHandler: function(form) {
			$.ajax({
				url: $register.attr("action"),
				type: "POST",
				data: {
					username: $username.val(),
					password: $password.val(),
					email: $email.val()
				},
				dataType: "json",
				cache: false,
				success: function(message) {
					$.message(message);
					if (message.type == "success") {
						window.setTimeout(function() {
							location.href = "index.jhtml";
						}, 3000);
					} else {
						
						alert(message.content);
					}
					
				}
			});
	    }

	  });

}); 



