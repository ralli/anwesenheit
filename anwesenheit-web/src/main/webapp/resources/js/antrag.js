var app = angular.module("antrag", [ "ngResource" ]);
app.config(function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl : 'index.html',
        controller : 'ListCtrl'
    }).when("/new", {
        templateUrl : 'new.html',
        controller : 'NewCtrl',
    }).when("/:id", {
        templateUrl : 'details.html',
        controller : 'DetailsCtrl',
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
    $http.get("/anwesenheit-web/api/antraege/" + $routeParams.id).success(
            function(data, status, headers, config) {
                $scope.antrag = angular.fromJson(data);
            }).error(function(data, status, headers, config) {
        alert("Antrag nicht gefunden");
    });
});

function parseDate(s) {
    var m = /([0-9][0-9]?).([0-9][0-9]?).([0-9]{4})/.exec(s);
    return m[3] + "-" + m[2] + "-" + m[1];
}

app.controller("NewCtrl", function($scope, $http) {
    $scope.antragArtListe = [ {
        antragArt : "URLAUB",
        position : 1,
        bezeichnung : "Urlaub"
    }, {
        antragArt : "GLEITTAG",
        position : 2,
        bezeichnung : "Gleittag"
    }, {
        antragArt : "SONDER",
        position : 3,
        bezeichnung : "Sonderurlaub"
    } ];
    $scope.antrag = {
        antragArt : $scope.antragArtListe[0],
        von : "01.01.2013",
        bis : "23.02.2013",
        bewilliger : []
    };

    $scope.createAntrag = function() {
        if ($scope.createForm.$valid) {
            var antragsDaten = {
                antragArt : $scope.antrag.antragArt.antragArt,
                von : parseDate($scope.antrag.von),
                bis : parseDate($scope.antrag.bis),
                bewilliger : $.map($scope.antrag.bewilliger, function(b) {
                    return b.benutzerId
                })
            };
            $http.post("/anwesenheit-web/api/antraege",
                    angular.toJson(antragsDaten)).success(function(data) {
                console.log(data);
            }).error(function(data) {
                console.log(data);
            });

        } else
            console.log("Fehler");
    };

    $scope.controlClassFor = function(flag) {
        var result;
        if (flag) {
            result = "control-group";
        } else {
            result = "control-group error";
        }
        return result;
    };

    $scope._addBewilliger = function(benutzerId) {
        if (_.find($scope.antrag.bewilliger, function(b) { return _.isEqual(b.benutzerId, benutzerId); })) {
            return;
        }            
        $http.get("/anwesenheit-web/api/benutzer/" + benutzerId).success(
                function(data) {
                    var benutzerDaten = angular.fromJson(data);
                    $scope.antrag.bewilliger.push(benutzerDaten);
                    $scope.bewilligerKey = "";
                }).error(function(data) {
            console.log(data);
        });
    };
    
    $scope.addBewilliger = function() {
        if ($scope.bewilligungForm.$valid) {
            var benutzerId = $scope.bewilligerKey;
            return $scope._addBewilliger(benutzerId);
        }
    };

    
    $scope.benutzerSelected = function(ui) {
        $scope._addBewilliger(ui.value);
    }
    
    $scope.removeBewilliger = function(b) {
        var array = $scope.antrag.bewilliger;
        for ( var i = array.length; i >= 0; i--) {
            if (_.isEqual(array[i], b)) {
                array.splice(i, 1);
                break;
            }
        }
    };
});


app.directive("benutzerAutocomplete", function($timeout) {
    return {
        restrict : 'A',
        link : function(scope, iElement, attr, ctrl) {
            iElement.autocomplete({
                source : "/anwesenheit-web/api/benutzer/search",
                select : function(event, ui) {
//                    $timeout(function() {
//                       iElement.trigger('input');
//                    }, 0);
                    scope.bewilligerKey = ui.item.value;
                    scope.$digest();
                    scope._addBewilliger(ui.item.value);
                 },
                minLength : 2
            });
        }
    };
});