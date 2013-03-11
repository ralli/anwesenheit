<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Anträge</title>
</head>
<body>
	<div ng-app="antrag">
		<div ng-init="antragListe=<c:out value="${antragListe}"/>">
			<div ng-view></div>
		</div>
		<p>
		
		<script type="text/ng-template" id="index.html">
			<h1>Offene Anträge</h1>
			<p><strong>Benutzer:</strong> {{antragListe.benutzer.vorname}}
				{{antragListe.benutzer.nachname}}</p>
			<a ng-href="#/new" class="btn" href><i
				class="icon-plus"></i>Neuer Antrag</a></p>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>Art</th>
						<th>Status</th>
						<th>Von</th>
						<th>Bis</th>
						<th>Aktion</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="antrag in antragListe.antraege">
						<td>{{antrag.antragArt.bezeichnung}}</td>
						<td>{{antrag.antragStatus.bezeichnung}}</td>
						<td>{{antrag.von|date:'dd.MM.yyyy'}}</td>
						<td>{{antrag.bis|date:'dd.MM.yyyy'}}</td>
						<td><a ng-href="#/{{antrag.id}}" href>Details</a>
					</tr>
				</tbody>
			</table>
		</script>
		<script type="text/ng-template" id="new.html">
			<h1>Neuer Antrag</h1>
			<form name="createForm" class="form-horizontal" ng-submit="createAntrag()">
               <legend>Antrag</legend>
               <div class="control-group">
                  <label class="control-label" for="antrag_art">Art</label>
                  <div class="controls">
                     <select id="antrag_art" name="antragArt" ng-model="antrag.antragArt" ng-options="a.bezeichnung for a in antragArtListe" />
                  </div>
               </div>
               <div ng-class="controlClassFor(createForm.von.$valid)">
                  <label for="antrag_von" class="control-label">Von</label>
                  <div class="controls">
                     <input type="text" id="antrag_von" name="von" ng-model="antrag.von" ng-pattern="/[0-9]{2}\.[0-9]{2}\.[0-9]{4}/" required />
                  </div>
               </div>
               <div ng-class="controlClassFor(createForm.bis.$valid)">
                  <label for="antrag_bis" class="control-label">Bis</label>
                  <div class="controls">
                     <input type="text" id="antrag_bis" name="bis" ng-model="antrag.bis"  ng-pattern="/[0-9]{2}\.[0-9]{2}\.[0-9]{4}/" required />
                  </div>
               </div>
  			   <div class="control-group">
                  <div class="controls">
                     <input type="submit" class="btn btn-primary" value="Speichern"/>
                     <a ng-href="#" class="btn">Zurück</a>
                  </div>
               </div>
            </form>
            
            <form name="bewilligungForm" class="form-horizontal" ng-submit="addBewilliger()">
               <legend>Bewilligungen</legend>
               <div class="control-group">
                  <label class="control-label" for="bewilliger_key">Bewilliger</label>
                  <div class="controls">
                     <input type="text" id="bewilliger_key" name="bewilliger" ng-model="bewilligerKey" required />
                     <input type="submit" class="btn btn-primary" value="Hinzufügen" />
                  </div>
               </div>                
            </form>
            <ul>
              <li ng-repeat="b in antrag.bewilliger">{{b}}</b>
            </ul>
            <p>{{createForm.$valid|json}}</p>
		</script>
		<script type="text/ng-template" id="details.html">
			<h1>Antrag</h1>
			<form class="form-horizontal">
               <legend>Antrag</legend>
               <div class="control-group">
                  <label class="control-label">Antragsteller</label>
                  <div class="controls">
                     <span class="uneditable-input">{{antrag.benutzer.vorname}} {{antrag.benutzer.nachname}}</span>
                  </div>
               </div>
               <div class="control-group">
                  <label class="control-label">Antragsart</label>
                  <div class="controls">
                     <span class="uneditable-input">{{antrag.antragArt.bezeichnung}}</span>
                  </div>
               </div> 			   
               <div class="control-group">
                  <label class="control-label">Antragsstatus</label>
                  <div class="controls">
                     <span class="uneditable-input">{{antrag.antragStatus.bezeichnung}}</span>
                  </div>
               </div>
               <div class="control-group">
                  <label class="control-label">Von</label>
                  <div class="controls">
                     <span class="uneditable-input">{{antrag.von|date:'dd.MM.yyyy'}}</span>
                  </div>
               </div> 			   
               <div class="control-group">
                  <label class="control-label">Bis</label>
                  <div class="controls">
                     <span class="uneditable-input">{{antrag.bis|date:'dd.MM.yyyy'}}</span>
                  </div>
               </div> 			    			   
			   <div class="control-group">
                  <div class="controls">
                     <a ng-href="#" class="btn">Zurück</a>
                  </div>
               </div>
			</form>
			<h2>Bewilligungen</h2>
			<table class="table table-bordered">
				<thead>
					<tr>
						<th>Bewilliger</th>
						<th>Status</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="bewilligung in antrag.bewilligungen">
						<td>{{bewilligung.benutzer.vorname}} {{bewilligung.benutzer.nachname}}</td>
						<td>{{bewilligung.bewilligungsStatus.bezeichnung}}</td>
					</tr>
				</tbody>
			</table>
		</script>
	</div>
	<script src="<c:url value="/resources/js/angular.min.js"/>"></script>
	<script src="<c:url value="/resources/js/angular-resource.min.js"/>"></script>
	<script src="<c:url value="/resources/js/antrag.js?reload=1"/>"></script>
</body>
</html>
