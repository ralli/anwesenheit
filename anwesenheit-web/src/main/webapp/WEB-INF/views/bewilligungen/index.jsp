<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Bewilligungen</title>
</head>
<body>
	<div ng-app="bewilligung">
		<div ng-view></div>
		<script type="text/ng-template" id="index.html">
			<h1>Bewilligungen</h1>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <td>Antragsteller</td>
                        <td>Art</td>
                        <td>Von</td>
                        <td>Bis</td>
                        <td>Status</td>
                        <td>Aktionen</td>
                    </tr>
                </thead>
                <tbody>
                   <tr ng-repeat="b in bewilligungsListe.bewilligungen">
                      <td>{{b.benutzer.vorname}} {{b.benutzer.nachname}}</td>
                      <td>{{b.antragArt.bezeichnung}}</td>
                      <td>{{b.von|date:"dd.MM.yyyy"}}</td>
                      <td>{{b.bis|date:"dd.MM.yyyy"}}</td>
                      <td>{{b.bewilligungsStatus.bezeichnung}}</td>
                      <td>
                        <a href="" class="btn btn-small btn-success">Bewilligen</a>
                        <a href="" class="btn btn-small btn-danger">Ablehnen</a></td>
                      </td>
                   </tr>
                </tbody>
            </table>            
            <p>{{bewilligungsListe|json}}</p>
        </script>
	</div>
	<script src="<c:url value="/resources/js/angular.min.js"/>"></script>
	<script src="<c:url value="/resources/js/angular-resource.min.js"/>"></script>
	<script src="<c:url value="/resources/js/bewilligung.js?reload=1"/>"></script>
</body>
</html>
