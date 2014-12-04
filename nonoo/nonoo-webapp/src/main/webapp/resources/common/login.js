//
//	jQuery Validate example script
//
jQuery.validator.addMethod("alnum", function (value, element) {  
    return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);  
}, "只能包括英文字母和数字");  

$().ready(function() {

	var $loginForm = $("#loginForm");
	var $username = $("#username");
	var $password = $("#password");
	
	var $captchaImage = $("#captchaImage");
	var $isRememberUsername = $("#isRememberUsername");
	var $submit = $(":submit");
	
     var username=$.cookie("username");
	if (username!= null) {
		$isRememberUsername.prop("checked", true);
		$username.val(username);
		$password.focus();
	} else {
		$isRememberUsername.prop("checked", false);
		$username.focus();
	}
	

	$captchaImage.click(function() {
		$captchaImage.attr("src", "common/captcha.jhtml?captchaId=${captchaId}&timestamp=" + (new Date()).valueOf());
	});
	

	$loginForm.validate({
		 rules: {
		      username: {
		    	  required: true,
		    	  rangelength:[6,24],
		    	 
		      },
		      password: {  
			     required: true,  
			     maxlength: 32,  
			     minlength: 6
			   }
		    },
		    messages: {
			      username: {  
			       required: "用户名不能为空！",  
			       rangelength:"用户名长度只能在6-18位字符之间",
			     },
			      password:{
				    required: "密码不能够为空",
				    minlength:"密码长度最少6位",
				    maxlength:"密码长度最多32位",
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
				url: "common/public_key.jhtml",
				type: "GET",
				dataType: "json",
				cache: false,
				beforeSend: function() {
					$submit.prop("disabled", true);
				},
				success: function(data) {
					$.ajax({
						url: $loginForm.attr("action"),
						type: "POST",
						data: {
							username: $username.val(),
							password: $password.val()
						},
						dataType: "json",
						cache: false,
						success: function(message) {
							if ($isRememberUsername.prop("checked")) {
								 $.cookie("username", $username.val(), {expires: 7 * 24 * 60 * 60});
							} else {
								 $.cookie("username","",{expires: -1});
							}
							$submit.prop("disabled", false);
							if (message.type == "success") {
								location.href = "member/index.jhtml";
							} else {
								$.message(message);
								
							}
						}
					});
				}
			});
		}
	});

});




