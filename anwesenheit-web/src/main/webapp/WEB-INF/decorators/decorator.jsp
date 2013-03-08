<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title><sitemesh:write property='title' /></title>
<c:url value="/" var="baseUrl" />
<script src="${baseUrl}/bootstrap/js/jquery.js"></script>
<script src="${baseUrl}/resources/js/application.js" charset="UTF-8"></script>
<sitemesh:write property='head' />
<meta charset="utf-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<!-- Le styles -->
<link href="<c:url value="/bootstrap/css/bootstrap.css" />" rel="stylesheet" />
<link href="<c:url value="/bootstrap/css/bootstrap-responsive.css" />" rel="stylesheet" />
<link href="<c:url value="/resources/test.css" />" rel="stylesheet" />

<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->

<!-- Le fav and touch icons -->
<link rel="shortcut icon" href="<c:url value="/bootstrap/ico/favicon.ico" />" />
<link rel="apple-touch-icon-precomposed" sizes="144x144"
  href="<c:url value="/bootstrap/ico/apple-touch-icon-144-precomposed.png" />" />
<link rel="apple-touch-icon-precomposed" sizes="114x114"
  href="<c:url value="/bootstrap/ico/apple-touch-icon-114-precomposed.png" />" />
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="<c:url value="/bootstrap/ico/apple-touch-icon-72-precomposed.png" />" />
<link rel="apple-touch-icon-precomposed" href="<c:url value="/bootstrap/ico/apple-touch-icon-57-precomposed.png" />" />
</head>
<body>
  <div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
      <div class="container">
        <a class="brand" href="<c:url value="/" />">Anwesenheit</a>
        <div class="nav-collapse">
          <ul class="nav">
            <li><a href="<c:url value="/" />">Home</a></li>            
          </ul>
        </div>
        <!--/.nav-collapse -->
      </div>
    </div>
  </div>
  <div class="container">
    <c:if test="${not empty message}">
      <div class="alert alert-success">
         ${message}         
      </div>
    </c:if>
    <sitemesh:write property='body' />
  </div>


  <script src="<c:url value="/bootstrap/js/bootstrap-transition.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-alert.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-modal.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-dropdown.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-scrollspy.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-tab.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-tooltip.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-popover.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-button.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-collapse.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-carousel.js" />"></script>
  <script src="<c:url value="/bootstrap/js/bootstrap-typeahead.js" />"></script>
</body>
</html>
