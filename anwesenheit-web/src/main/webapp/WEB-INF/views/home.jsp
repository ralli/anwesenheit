<!DOCTYPE html>
<%@page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/bootstrap" prefix="b" %>
<html ng-app="app">
<head>
  <title>Anwesenheiten</title>
  <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>

  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Le styles -->
  <link href="<c:url value="/webjars/bootstrap/2.3.1/css/bootstrap.min.css" />" rel="stylesheet"/>
  <link href="<c:url value="/webjars/bootstrap/2.3.1/css/bootstrap-responsive.min.css" />" rel="stylesheet"/>
  <link href="<c:url value="/webjars/font-awesome/3.0.2/css/font-awesome.min.css" />" rel="stylesheet"/>
  <link href="<c:url value="/webjars/jquery-ui/1.10.2/themes/base/minified/jquery-ui.min.css" />" rel="stylesheet"/>
  <link href="<c:url value="/webjars/angular-ui/0.4.0/angular-ui.min.css" />" rel="stylesheet"/>
  <link href="<c:url value="/resources/lib/toastr/toastr.css" />" rel="stylesheet"/>
  <link href="<c:url value="/resources/css/main.css" />" rel="stylesheet"/>

  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>
<body>
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
      <div class="container nav-collapse">
        <a class="brand" href="<c:url value="#!" />">Anwesenheit</a>
        <ul class="nav pull-right">
          <li class="navbar-text">
            <b:currentUserName id="current-user-name">Username</b:currentUserName>
          </li>
          <li>
            <a href="<c:url value="/logoff" />"><i class="icon-off"></i> Abmelden</a>
          </li>
        </ul>
          <ul class="nav">
              <li><a href="<c:url value="#!/antraege" />"><i class="icon-tasks"></i> Anträge</a></li>
              <li><a href="<c:url value="#!/bewilligungen" />"><i class="icon-ok"></i> Bewilligungen</a></li>
              <li><a href="<c:url value="#!/uebersicht" />"><i class="icon-calendar"></i> Übersicht</a></li>
              <c:if test="${hatSonderBerechtigung}">
                  <li><a href="<c:url value="#!/reports" />"><i class="icon-bar-chart"></i> Reports</a></li>
              </c:if>
          </ul>
      </div>
    </div>
  </div>
</div>
<div class="container">
  <div ng-view></div>
</div>
<script src="<c:url value="/webjars/jquery/2.0.0/jquery.min.js"/>"></script>
<script src="<c:url value="/webjars/jquery-ui/1.10.2/ui/minified/jquery-ui.min.js"/>"></script>
<script src="<c:url value="/webjars/jquery-ui/1.10.2/ui/i18n/jquery.ui.datepicker-de.js"/>"></script>
<script src="<c:url value="/webjars/angularjs/1.1.4/angular.min.js"/>"></script>
<script src="<c:url value="/webjars/angularjs/1.1.4/angular-resource.min.js"/>"></script>
<script src="<c:url value="/webjars/angularjs/1.1.4/angular-bootstrap.min.js"/>"></script>
<script src="<c:url value="/webjars/angularjs/1.1.4/i18n/angular-locale_de.js"/>"></script>
<script src="<c:url value="/webjars/angular-ui/0.4.0/angular-ui.min.js"/>"></script>
<script src="<c:url value="/webjars/angular-ui/0.4.0/angular-ui-ieshiv.min.js"/>"></script>
<script src="<c:url value="/webjars/bootstrap/2.3.1/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/webjars/angular-ui-bootstrap/0.3.0/ui-bootstrap-tpls.min.js" />"></script>
<script src="<c:url value="/webjars/underscorejs/1.4.4/underscore.min.js"/>"></script>
<script src="<c:url value="/resources/lib/toastr/toastr.js" />"></script>
<script src="<c:url value="/resources/lib/showdown/showdown-min.js" />"></script>
<script src="<c:url value="/resources/js/services.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/helpers.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/controllers/antrag.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/controllers/bewilligung.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/controllers/uebersicht.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/controllers/reports.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/directives.js?reload=1"/>" charset="UTF-8"></script>
<script src="<c:url value="/resources/js/app.js?reload=1"/>" charset="UTF-8"></script>

</body>
</html>
