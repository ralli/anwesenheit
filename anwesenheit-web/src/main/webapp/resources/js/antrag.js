var module = angular.module("antrag", []);
module.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'views/details.html',
		controller : 'DetailsCtrl'
	})
});

module.controller("MainCtrl", function($scope) {
	$scope.greeting = "Wunder";
});

module.controller("DetailsCtrl", function($scope) {
	$scope.antrag = {
		"id" : 10,
		"antragArt" : "URLAUB",
		"von" : "2013-04-01T00:00+0200",
		"bis" : "2013-04-20T00:00+0200",
		"benutzer" : {
			"benutzerId" : "demo123",
			"vorname" : "King",
			"nachname" : "Kong",
			"email" : "demo@demo.de"
		},
		"bewilligungen" : [ {
			"id" : 1234,
			"antragId" : 10,
			"bewilligungsStatus" : "OFFEN",
			"benutzer" : {
				"benutzerId" : "chef123",
				"vorname" : "King",
				"nachname" : "Kong",
				"email" : "demo@demo.de"
			}
		} ]
	};
});
