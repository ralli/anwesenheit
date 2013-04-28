<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>Login</title>
<link href="<c:url value="/bootstrap/css/bootstrap.min.css" />" rel="stylesheet" />
<link href="<c:url value="/font-awesome/css/font-awesome.min.css" />" rel="stylesheet" />
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
	background-color: #f5f5f5;
}

.form-signin {
	max-width: 300px;
	padding: 19px 29px 29px;
	margin: 0 auto 20px;
	background-color: #fff;
	border: 1px solid #e5e5e5;
	-webkit-border-radius: 5px;
	-moz-border-radius: 5px;
	border-radius: 5px;
	-webkit-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	-moz-box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
	box-shadow: 0 1px 2px rgba(0, 0, 0, .05);
}

.form-signin .form-signin-heading,.form-signin .checkbox {
	margin-bottom: 10px;
}

.form-signin input[type="text"],.form-signin input[type="password"] {
	font-size: 16px;
	height: auto;
	margin-top: 15px;
	margin-bottom: 0px;
	padding: 7px 9px;
}

.form-signin button {
    margin-top: 15px;
}

.fieldError {
   color: #ff0000;
   padding: 0px;
   margin: 0px;
}
</style>
</head>
<body>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<a class="brand" href="<c:url value="/" />">Anwesenheit</a>
			</div>
		</div>
	</div>
	<form:form id="login-form" cssClass="form-signin" modelAttribute="loginCommand"
		method="POST">
		<form:errors  cssClass="fieldError"/>
		<h2 class="form-signin-heading">Bitte melden Sie sich an</h2>
		<form:input path="login" placeholder="Benutzername" />  <form:errors path="login" element="div" cssClass="fieldError"/>
		<form:password path="password" placeholder="Passwort" /> <form:errors path="password" element="div"  cssClass="fieldError"/>
		<button class="btn btn-large btn-primary" type="submit"><i class="icon-ok"></i>Anmelden</button>
	</form:form>
</body>
</html>
