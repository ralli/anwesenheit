<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<div class="hero-unit" ng-app>
		<h1>Anwesenheitsverwaltung</h1>
		<p>Spring Web MVC Demonstration</p>
		<p>
		<ul>
			<li>1+1={{1+1}}</li>
			<li>Meine Anträge</li>
			<li>Meine Bewilligungen</li>
			<li>Meine Eintragungen</li>
		</ul>
		</p>
	</div>
	<script src="<c:url value="/resources/js/angular.min.js"/>"></script>
</body>
</html>
