<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Home</title>
</head>
<body>
	<div class="hero-unit" ng-app="antrag">
		<div ng-controller="MainCtrl">
			<p ng-view></p>
		</div>
		<script type="text/ng-template" id="views/details.html">
			<p>Antragsteller: {{antrag.benutzer.vorname}} {{antrag.benutzer.nachname}}</p>
			<p>Antragsart: {{antrag.antragArt}}</p>
			<p>Von: {{antrag.von}}</p>
			<p>Bis: {{antrag.bis}}</p>
	    </script>
	</div>
	<script src="<c:url value="/resources/js/angular.min.js"/>"></script>
	<script src="<c:url value="/resources/js/antrag.js"/>"></script>
</body>
</html>
