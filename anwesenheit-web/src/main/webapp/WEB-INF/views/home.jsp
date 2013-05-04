<!DOCTYPE html>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/bootstrap" prefix="b"%>
<html ng-app="antrag">
<head>
<title>Anwesenheiten</title>
<meta charset="utf-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link href="<c:url value="webjars/bootstrap/2.3.1/css/bootstrap.min.css" />" rel="stylesheet" />
<link href="<c:url value="webjars/bootstrap/2.3.1/css/bootstrap-responsive.min.css" />" rel="stylesheet" />
<link href="<c:url value="webjars/bootstrap-datepicker/1.0.1/css/datepicker.css" />" rel="stylesheet" />
<link href="<c:url value="webjars/bootstrap-timepicker/0.2.1/css/bootstrap-timepicker.min.css" />" rel="stylesheet" />
<link href="<c:url value="webjars/font-awesome/3.0.2/css/font-awesome.min.css" />" rel="stylesheet" />
<link href="<c:url value="webjars/jquery-ui/1.10.2/themes/base/minified/jquery-ui.min.css" />" rel="stylesheet" />
<link href="<c:url value="webjars/angular-ui/0.4.0/angular-ui.min.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/toastr/toastr.css" />" rel="stylesheet" />


<link href="<c:url value="/resources/test.css?reload=1" />" rel="stylesheet" />


<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
		      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->

<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<c:url value="/bootstrap/ico/favicon.ico" />" />
</head>
<body>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<div class="container nav-collapse">
					<a class="brand" href="<c:url value="#!" />"><i class="icon-home">&nbsp;</i>Anwesenheit</a>
					<ul class="nav">
						<li><a href="<c:url value="#!/antraege" />"><i class="icon-tasks"></i>&nbsp;Anträge</a></li>
						<li><a href="<c:url value="#!/bewilligungen" />"><i class="icon-ok"></i>&nbsp;Bewilligungen</a></li>
						<li><a href="<c:url value="#!/uebersicht" />"><i class="icon-calendar"></i>&nbsp;Übersicht</a></li>
					</ul>icon-user
					<div class="btn-group">
  						<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
  							<em><b:currentUserName id="current-user-name">-</b:currentUserName></em>
						    <span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li><a href="#!">Einstellungen</a></li>
							<li><a href="#!">Logout</a></li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<alert ng-repeat="alert in alerts" type="alert.type" close="closeAlert($index)">{{alert.msg}}</alert>
		<div ng-view></div>
	</div>
	<script src="<c:url value="webjars/bootstrap/2.3.1/js/bootstrap.min.js" />"></script>
	<script src="<c:url value="webjars/bootstrap-datepicker/1.0.1/js/bootstrap-datepicker.js" />"></script>
	<script src="<c:url value="webjars/bootstrap-timepicker/0.2.1/js/bootstrap-timepicker.min.js" />"></script>
	<script src="<c:url value="webjars/jquery/2.0.0/jquery.min.js"/>"></script>
	<script src="<c:url value="webjars/jquery-ui/1.10.2/ui/jquery-ui.js"/>"></script>
	<script src="<c:url value="webjars/jquery-ui/1.10.2/themes/base/minified/jquery.ui.datepicker.min.css"/>"></script>
	<script src="<c:url value="webjars/angularjs/1.1.4/angular.min.js"/>"></script>
	<script src="<c:url value="webjars/angularjs/1.1.4/angular-resource.min.js"/>"></script>
	<script src="<c:url value="webjars/angularjs/1.1.4/angular-bootstrap.min.js"/>"></script>
	<script src="<c:url value="webjars/angularjs/1.1.4/i18n/angular-locale_de.js"/>"></script>
	<script src="<c:url value="webjars/angular-ui/0.4.0/angular-ui.min.js"/>"></script>
	<script src="<c:url value="webjars/underscorejs/1.4.4/underscore.min.js"/>"></script>
	<script src="<c:url value="resources/toastr/toastr.js" />"></script>
	<script src="<c:url value="resources/js/antrag.js?reload=1"/>" charset="UTF-8"></script>
</body>
</html>
