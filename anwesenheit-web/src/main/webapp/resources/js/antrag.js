var app = angular.module("antrag", [ "ngResource" ]);
app.config(function($routeProvider) {
    $routeProvider.when('/', {
        templateUrl : 'index.html',
        controller : 'ListCtrl'
    }).when("/new", {
        templateUrl : 'new.html',
        controller : 'NewCtrl',
    })
    .when("/:id/edit", {
        templateUrl : 'edit.html',
        controller : 'EditCtrl',        
    })
    .when("/:id", {
        templateUrl : 'details.html',
        controller : 'DetailsCtrl',
    })
});

app.factory("antragService", function($resource) {
    return $resource("/anwesenheit-web/api/antraege/:id");
});

app.factory("antragArtService", function() {
    var result = {};
    result.get = function(id, success, error) {
        var x = [ {
            antragArt : "URLAUB",
            position : 1,
            bezeichnung : "Urlaub"
        }, {
            antragArt : "GLEITZEIT",
            position : 2,
            bezeichnung : "Gleitzeit"
        }, {
            antragArt : "SONDER",
            position : 3,
            bezeichnung : "Sonderurlaub"
        } ];
        if(success) 
            success.call({}, x);
        return x;
    };
    return result;
});

app.factory("benutzerService", function($resource) {
    return $resource("/anwesenheit-web/api/benutzer/:id");
});

app.controller("AppCtrl", function($rootScope) {
    $rootScope.$on("$routeChangeError", function() {
        console.log("Error changing routes");
    })
});

app.controller("ListCtrl", function($scope, antragService) {
    $scope.deleteAntrag = function(antrag) {
        antragService.delete({ "id": antrag.id }, function(data) {
            $scope.antragListe = antragService.get();        
        });
    }
});

app.controller("DetailsCtrl", function($scope, $routeParams, antragService) {
    $scope.antrag = antragService.get({
        "id" : $routeParams.id
    });
});

function parseDate(s) {
    var m = /([0-9][0-9]?).([0-9][0-9]?).([0-9]{4})/.exec(s);
    return m[3] + "-" + m[2] + "-" + m[1];
}

app.controller("NewCtrl", function($scope, antragService, antragArtService,
        benutzerService) {
    $scope.antragArtListe = antragArtService.get({}, function(liste) {
        $scope.antrag = {
            antragArt : _.clone(liste[0]),
            von : "01.01.2013",
            bis : "23.02.2013",
            bewilliger : []
        };
    });

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

            antragService.save(angular.toJson(antragsDaten), function(data) {
                console.log(data);
            }, function(data) {
                console.log(data);
            });
        }
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
        if (_.find($scope.antrag.bewilliger, function(b) {
            return _.isEqual(b.benutzerId, benutzerId);
        })) {
            return;
        }
        benutzerService.get({
            "id" : benutzerId
        }, function(benutzerDaten) {
            $scope.antrag.bewilliger.push(benutzerDaten);
            $scope.bewilligerKey = "";
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

app.controller("EditCtrl", function($scope, $routeParams, $filter, antragArtService,
        antragService, benutzerService) {
    $scope.antragArtListe = antragArtService.get({});
    $scope.antrag = antragService.get({
        "id" : $routeParams.id
    }, function(data) {
        data.von = $filter("date")(data.von, "dd.MM.yyyy");
        data.bis = $filter("date")(data.bis, "dd.MM.yyyy");
        console.log(data);
    }, function(data) {
        console.log(data);
    });
    
    $scope.controlClassFor = function(flag) {
        var result;
        if (flag) {
            result = "control-group";
        } else {
            result = "control-group error";
        }
        return result;
    };
    
    $scope.saveAntrag = function() {
        
    };
    
    $scope.deleteBewilliger = function(b) {
        
    };
});

app.directive("benutzerAutocomplete", function($timeout) {
    return {
        restrict : 'A',
        link : function(scope, iElement, attr, ctrl) {
            iElement.autocomplete({
                source : "/anwesenheit-web/api/benutzer/search",
                select : function(event, ui) {
                    scope.bewilligerKey = ui.item.value;
                    scope.$digest();
                    scope._addBewilliger(ui.item.value);
                },
                minLength : 2
            });
        }
    };
});
