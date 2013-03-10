var app = angular.module("antrag", ["ngResource"]);
app.config(function($routeProvider) {
	$routeProvider.when('/', {
		templateUrl : 'index.html',
		controller : 'ListCtrl'
	})
	.when("/new", {
		templateUrl: 'new.html',
		controller: 'NewCtrl',		
	})
	.when("/:id", {
		templateUrl: 'details.html',
		controller: 'DetailsCtrl',		
	})
});

app.controller("AppCtrl", function($rootScope) {
	$rootScope.$on("$routeChangeError", function() {
		console.log("Error changing routes");
	})
});

app.controller("ListCtrl", function($scope) {
	
});

app.controller("DetailsCtrl", function($scope, $http, $routeParams) {
	$http.get("/anwesenheit-web/api/antraege/" + $routeParams.id)
		.success(function(data, status, headers, config) {
			$scope.antrag = angular.fromJson(data);
		})
		.error(function(data, status, headers, config) {
			alert("Antrag nicht gefunden");
		});		
});

app.controller("NewCtrl", function($scope) {
	$scope.antragArtListe = [
	                  	   { antragArt: "URLAUB", position: 1, bezeichnung: "Urlaub" },
	                  	   { antragArt: "GLEITTAG", position: 2, bezeichnung: "Gleittag" },
	                  	   { antragArt: "SONDER", position: 3, bezeichnung: "Sonderurlaub"}
	                  	];
	$scope.antrag = {
		antragArt: $scope.antragArtListe[0],
		von: "01.01.2013",
		bis: "23.02.2013",
		bewilliger: []
	};
	
	$scope.createAntrag = function() {
		if($scope.createForm.$valid)
			console.log($scope.antrag);
		else
			console.log("Fehler");
	};
	
	$scope.controlClassFor = function(flag) {
		var result;
		if(flag) {
			result = "control-group";
		}
		else {
			result = "control-group error";
		}
		return result;
	};
	$scope.addBewilliger = function() {
		$scope.antrag.bewilliger.push($scope.bewilligerKey);
	};
});

app.directive('autocomplete', ['$http', function($http) {
    return function (scope, element, attrs) {
        element.autocomplete({
            minLength:3,
            source:function (request, response) {
                var url = "/anwesenheit-web/api/benutzer/search?q=" + request.term;
                $http.get(url).success( function(data) {
                    response(data.results);
                });
            },
            focus:function (event, ui) {
                element.val(ui.item.label);
                return false;
            },
            select:function (event, ui) {
                scope.myModelId.selected = ui.item.value;
                scope.$apply;
                return false;
            },
            change:function (event, ui) {
                if (ui.item === null) {
                    scope.myModelId.selected = null;
                }
            }
        }).data("autocomplete")._renderItem = function (ul, item) {
            return $("<li></li>")
                .data("item.autocomplete", item)
                .append("<a>" + item.label + "</a>")
                .appendTo(ul);
        };
    }
}]);