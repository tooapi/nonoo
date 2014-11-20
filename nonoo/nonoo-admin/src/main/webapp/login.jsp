<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
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
  <nav class="navbar navbar-inverse " >
  <div class="container">
  <div class="navbar-header">
    <button type="button" class="navbar-toggle" data-toggle="collapse" >
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
    <a class="navbar-brand" href="#">Nonoo</a>
  </div>
  <div class="collapse navbar-collapse" >
    <ul class="nav navbar-nav navbar-right">
      <li><a href="/member/login.php">登陆</a></li>
      <li><a href="/member/index_do.php?fmdo=user&dopost=regnew">注册</a></li>
    </ul>
    
  </div>
  </div>
</nav>
   
</header>
 
<!-- /header -->


	<div class="container">

		<div class="row">
			<div class="panel panel-default">
				<div class="panel-heading"><h4>用户登录</h4></div>
				<div class="panel-body">
					<form action="" method="post">
					
					       <div class="form-group">
								<label class="control-label">用户名：</label>
						          <input type="text" class="form-control input-lg  " name="username" id="username" placeholder="">
							</div>
							
							 <div class="form-group">
								<label class="control-label">密&nbsp;&nbsp;码：</label>
						           <input type="password" class="form-control input-lg" name="password" id="password" placeholder="">
							</div>
					
						
                       <button class="btn btn-lg btn-primary btn-block" type="submit">登陆</button>
                      
					</form>

				</div>
			</div>

		</div>
	</div>
	
<script src="resources/jquery/jquery.min.js"></script>
<script src="resources/bootstrap/js/bootstrap.min.js"></script>
</body>


