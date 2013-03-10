<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Page not found</title>
<c:url value="/" var="baseUrl" />
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
      </div>
    </div>
  </div>
  <div class="container">
  <div class="hero-unit">
   <h1>Seite nicht gefunden</h1>
   <p>Die angeforderte Seite existiert nicht.</p>
   <a href="<c:url value="/" />" class="btn btn-primary btn-large">Startseite</a>
  </div>
  </div>
</body>
</html>
