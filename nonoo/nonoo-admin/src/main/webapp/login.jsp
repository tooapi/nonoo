<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="com.fdp.nonoo.common.Setting"%>
<%@page import="com.fdp.nonoo.util.SettingUtils"%>
<%@page import="com.fdp.nonoo.util.SpringUtils"%>
<%@page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@page import="java.util.UUID"%>
<%@page import="com.fdp.nonoo.common.Setting.AccountLockType"%>
<%@page import="org.apache.commons.lang.ArrayUtils"%>
<%@page import="com.fdp.nonoo.common.Setting.CaptchaType"%>
<%@page import="java.security.interfaces.RSAPublicKey"%>
<%@page import="org.apache.commons.codec.binary.Base64"%>
<%@page import="com.fdp.nonoo.service.RSAService"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%
	String base = request.getContextPath();
	String captchaId = UUID.randomUUID().toString();
	ApplicationContext applicationContext = SpringUtils.getApplicationContext();
	Setting setting = SettingUtils.get();
	String message = null;
	String loginFailure = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
	if (loginFailure != null) {
		if (loginFailure.equals("org.apache.shiro.authc.pam.UnsupportedTokenException")) {
			message = "admin.captcha.invalid";
		} else if (loginFailure.equals("org.apache.shiro.authc.UnknownAccountException")) {
			message = "admin.login.unknownAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.DisabledAccountException")) {
			message = "admin.login.disabledAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.LockedAccountException")) {
			message = "admin.login.lockedAccount";
		} else if (loginFailure.equals("org.apache.shiro.authc.IncorrectCredentialsException")) {
			if (ArrayUtils.contains(setting.getAccountLockTypes(), AccountLockType.admin)) {
				message = "admin.login.accountLockCount";
			} else {
				message = "admin.login.incorrectCredentials";
			}
		} else if (loginFailure.equals("org.apache.shiro.authc.AuthenticationException")) {
			message = "admin.login.authentication";
		}
	}
	System.out.println(message);
	if (applicationContext != null) {
%>
		<shiro:authenticated>
		<%response.sendRedirect(base + "/common/main.jhtml");%>
		</shiro:authenticated>
<%}%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="resources/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<title>登陆页面</title>
</head>
<body>
	<header id="header">
		<nav class="navbar navbar-inverse ">
			<div class="container">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle" data-toggle="collapse">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="#">Nonoo</a>
				</div>
				<div class="collapse navbar-collapse">
					<ul class="nav navbar-nav navbar-right">

					</ul>

				</div>
			</div>
		</nav>

	</header>

	<!-- /header -->
	<div class="container">

		<div class="row">
			<div class="panel panel-default">
				<div class="panel-heading">
					<h4>用户登录</h4>
				</div>
				<div class="panel-body">
					<form name="loginForm" action="login.jsp" method="post">
                        <input type="hidden" name="captchaId" value="<%=captchaId%>" />
						<div class="form-group">
							<label class="control-label">用户名：</label> <input type="text"
								class="form-control input-lg  " name="username" id="username" value="admin" placeholder="" required autofocus>
						</div>

						<div class="form-group">
							<label class="control-label">密&nbsp;&nbsp;码：</label> 
							<input type="password" class="form-control input-lg" name="password" id="password" placeholder="" value="admin" required>
						</div>

						<div class='form-group'>
							<label class='control-label '>验证码：</label>
							 <div class="row">
							 
								<div class="col-xs-6 ">
								   <input type="text" id="captcha" class="form-control input-lg" placeholder="" name='captcha' autocomplete="off" />
								   </div>
							    <div class="col-xs-6 ">
							       <img id="captchaImage" class="captchaImage" src="<%=base%>/common/captcha.jhtml?captchaId=<%=captchaId%>" />
							    </div>
							</div>
						</div>

						<div class="checkbox">
							<label> <input name="isRememberMe" id="isRememberMe" type="checkbox">下次自动登录 </label>
						</div>


						<button class="btn btn-lg btn-primary btn-block" type="submit">登陆</button>

					</form>

				</div>
			</div>

		</div>
	</div>

<script src="${base}resources/jquery/jquery.min.js"></script>
<script src="${base}resources/jquery/jquery.cookie.js"></script>
<script src="${base}resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$().ready( function() {
		
		var $loginForm = $("#loginForm");
		var $enPassword = $("#enPassword");
		var $username = $("#username");
		var $password = $("#password");
		var $captcha = $("#captcha");
		var $captchaImage = $("#captchaImage");
		var $isRememberMe = $("#isRememberMe");
		
		
		// 记住用户名
		 var adminUsername=$.cookie("adminUsername");
		if(adminUsername != null) {
			$isRememberMe.prop("checked", true);
			$username.val(adminUsername);
			$password.focus();
		} else {
			$isRememberMe.prop("checked", false);
			$username.focus();
		}
		
		// 更换验证码
		$captchaImage.click( function() {
			$captchaImage.attr("src", "<%=base%>/common/captcha.jhtml?captchaId=<%=captchaId%>&timestamp=" + (new Date()).valueOf());
		});
		
		// 表单验证、记住用户名
		$loginForm.submit( function() {
			
			
			if ($isRememberMe.prop("checked")) {
				$.cookie("adminUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
			} else {
				$.cookie("adminUsername","",{expires: -1});
			}
			
		});
		
		
	});
</script>
</body>