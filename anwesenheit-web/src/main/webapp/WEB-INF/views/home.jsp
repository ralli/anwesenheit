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
<link href="<c:url value="/bootstrap/css/bootstrap.min.css" />" rel="stylesheet" />
<link href="<c:url value="/bootstrap/css/bootstrap-responsive.min.css" />" rel="stylesheet" />
<link href="<c:url value="/font-awesome/css/font-awesome.min.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/jquery-ui.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/angular-ui.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/css/toastr.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/test.css?reload=1" />" rel="stylesheet" />

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<c:url value="/bootstrap/ico/favicon.ico" />" />
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="<c:url value="/bootstrap/ico/apple-touch-icon-144-precomposed.png" />" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="<c:url value="/bootstrap/ico/apple-touch-icon-114-precomposed.png" />" />
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value="/bootstrap/ico/apple-touch-icon-72-precomposed.png" />" />
<link rel="apple-touch-icon-precomposed" href="<c:url value="/bootstrap/ico/apple-touch-icon-57-precomposed.png" />" />
</head>
<body>
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">
				<div class="container nav-collapse">
					<a class="brand" href="<c:url value="#!" />">Anwesenheit</a>
					<ul class="nav">
						<li><a href="<c:url value="#!/antraege" />"><i class="icon-tasks"></i>Anträge</a></li>
						<li><a href="<c:url value="#!/bewilligungen" />"><i class="icon-ok"></i>Bewilligungen</a></li>
						<li><a href="<c:url value="#!/uebersicht" />"><i class="icon-calendar"></i>Übersicht</a></li>
					</ul>
					<b:currentUserName id="current-user-name">Username</b:currentUserName>
				</div>
			</div>
		</div>
	</div>
	<div class="container">
		<div ng-view></div>
	</div>
	<script src="<c:url value="/resources/js/jquery-1.7.2.min.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery-ui.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery.ui.datepicker-de.js"/>"></script>
	<script src="<c:url value="/resources/js/underscore-min.js"/>"></script>
	<script src="<c:url value="/resources/js/angular.min.js"/>"></script>
	<script src="<c:url value="/resources/js/angular-resource.min.js"/>"></script>
	<script src="<c:url value="/resources/js/angular-locale_de.js"/>"></script>
	<script src="<c:url value="/resources/js/angular-ui.min.js"/>"></script>
	<script src="<c:url value="/resources/js/antrag.js?reload=1"/>" charset="UTF-8"></script>
	<script src="<c:url value="/bootstrap/js/bootstrap.min.js" />"></script>
	<script src="<c:url value="/resources/js/ui-bootstrap-tpls-0.2.0.js" />"></script>
	<script src="<c:url value="/resources/js/toastr.js" />"></script>
</body>
</html>
