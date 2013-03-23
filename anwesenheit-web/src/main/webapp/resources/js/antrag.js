// Encoding: UTF-8

var app = angular.module("antrag", [ "ngResource", "ui", "ui.bootstrap" ]);
app.config(function($routeProvider) {
    $routeProvider
    .when('/', {
      templateUrl : '/anwesenheit-web/resources/partials/home.html',
      controller : 'HomeCtrl'
    })
    .when('/antraege', {
        templateUrl : '/anwesenheit-web/resources/partials/antraege/index.html',
        controller : 'ListAntragCtrl'
    })
    .when("/antraege/new", {
        templateUrl : '/anwesenheit-web/resources/partials/antraege/new.html',
        controller : 'NewAntragCtrl',
    })
    .when("/antraege/:id/edit", {
        templateUrl : '/anwesenheit-web/resources/partials/antraege/edit.html',
        controller : 'EditAntragCtrl',        
    })
    .when("/antraege/:id", {
        templateUrl : '/anwesenheit-web/resources/partials/antraege/details.html',
        controller : 'AntragDetailsCtrl',
    })
    .when("/bewilligungen", {
        templateUrl : '/anwesenheit-web/resources/partials/bewilligungen/index.html',
        controller : 'ListBewilligungCtrl',
    });
});

app.factory("antragService", function($resource) {
  return $resource("/anwesenheit-web/api/antraege/:id", {"id" : "@id" }, {
    "update" : { "method" : "PUT" }
  });
});

app.factory("antragArtService", function($resource) {
  return $resource("/anwesenheit-web/api/antragsarten/:id");
});

app.factory("sonderUrlaubArtService", function($resource) {
  return $resource("/anwesenheit-web/api/sonderurlaubarten/:id");
});

app.factory("benutzerService", function($resource) {
    return $resource("/anwesenheit-web/api/benutzer/:id");
});

app.factory("bewilligungService", function($resource) {
    return $resource("/anwesenheit-web/api/bewilligung/:id");
});

app.controller("AppCtrl", function($rootScope) {
    $rootScope.$on("$routeChangeError", function() {
        console.log("Error changing routes");
    });
});

app.controller("HomeCtrl", function($scope) {  
});

app.controller("ListAntragCtrl", function($scope, antragService) {
  $scope.antragListe = antragService.get({});
    $scope.deleteAntrag = function(antrag) {
        antragService.delete({ "id": antrag.id }, function(data) {
            $scope.antragListe.antraege = _.reject($scope.antragListe.antraege, function(a) { return a.id === antrag.id; });        
        });
    };
});

app.controller("AntragDetailsCtrl", function($scope, $routeParams, antragService) {
    $scope.antrag = antragService.get({
        "id" : $routeParams.id
    });
    
    $scope.sonderUrlaubArtVisible = function() {
      return $scope.antrag && $scope.antrag.antragArt.antragArt === "SONDER";
    };
});

function parseDate(s) {
    var m = /([0-9][0-9]?).([0-9][0-9]?).([0-9]{4})/.exec(s);
    return m[3] + "-" + m[2] + "-" + m[1];
}

function parseNumber(s) {
  var x = s.replace(/,/g, '.');
  return parseFloat(x);
}

app.controller("NewAntragCtrl", function($scope, $location, antragService, antragArtService,
        benutzerService, sonderUrlaubArtService) {
    $scope.antragArtListe = antragArtService.query(function(liste) {
        $scope.antrag = {
            antragArt : _.clone(liste[0]),
            sonderUrlaubArt : "UMZUG",
            von : "01.01.2013",
            bis : "23.02.2013",
            anzahlTage: "0",
            bewilliger : []
        };
    });
    
    $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();
    

    $scope.createAntrag = function() {
        if ($scope.createForm.$valid) {
            var antragsDaten = {
                antragArt : $scope.antrag.antragArt.antragArt,
                sonderUrlaubArt: $scope.antrag.sonderUrlaubArt,
                von : parseDate($scope.antrag.von),
                bis : parseDate($scope.antrag.bis),
                anzahlTage: parseNumber($scope.antrag.anzahlTage),
                bewilliger : $.map($scope.antrag.bewilliger, function(b) {
                    return b.benutzerId;
                })
            };

            antragService.save(angular.toJson(antragsDaten), function(data) {
                console.log(data);
                $location.url("/antraege");
            }, function(data) {
                console.log(data);
            });
        };
    };

    $scope.sonderUrlaubArtVisible = function() {
      return $scope.antrag && $scope.antrag.antragArt.antragArt === "SONDER";
    }

    $scope.anzahlTageVisible = function() {
      return !$scope.sonderUrlaubArtVisible();
    }
    
    $scope.getAnzahlTage = function() {
      if(!$scope.sonderUrlaubArtVisible())
        return 0;
      var x = _.find($scope.sonderUrlaubArtListe, function(a) {
        return _.isEqual(a.sonderUrlaubArt, $scope.antrag.sonderUrlaubArt);
      });
      return x.anzahlTage;
    }
    
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
      $scope.bewilligungError = "";
        if (_.find($scope.antrag.bewilliger, function(b) {
          return _.isEqual(b.benutzerId, benutzerId);
        })) {
          $scope.bewilligungError = "Die Bewilligung kann nicht mehrfach hinzugefügt werden";
          return;
        }
        benutzerService.get({"id" : benutzerId }, function(benutzerDaten) {
            $scope.antrag.bewilliger.push(benutzerDaten);
            $scope.bewilligerKey = "";
        }, function(data) {
          $scope.bewilligungError = data.data.message;
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
      $scope.antrag.bewilliger = _.reject($scope.antrag.bewilliger, function(x) {
        return _.isEqual(x,b);
      });
    };
});

app.controller("EditAntragCtrl", function($scope, $routeParams, $filter, $location, antragArtService,
        antragService, benutzerService, bewilligungService, sonderUrlaubArtService) {
    $scope.antragArtListe = antragArtService.query();
    $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();
    $scope.antrag = antragService.get({
        "id" : $routeParams.id
    }, function(data) {
        var s = _.isNull(data.sonderUrlaubArt) ? "UMZUG" : data.sonderUrlaubArt.sonderUrlaubArt;
        data.von = $filter("date")(data.von, "dd.MM.yyyy");
        data.bis = $filter("date")(data.bis, "dd.MM.yyyy");
        data.sonderUrlaubArt = s;
        data.anzahlTage = $filter("number")(data.anzahlTage);        
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
    
    $scope.sonderUrlaubArtVisible = function() {
      return $scope.antrag && $scope.antrag.antragArt && $scope.antrag.antragArt.antragArt === "SONDER";
    }
    
    $scope.anzahlTageVisible = function() {
      return !$scope.sonderUrlaubArtVisible();
    }
    
    $scope.getAnzahlTage = function() {
      if(!$scope.sonderUrlaubArtVisible())
        return 0;
      var x = _.find($scope.sonderUrlaubArtListe, function(a) {
        return _.isEqual(a.sonderUrlaubArt, $scope.antrag.sonderUrlaubArt);
      });
      return _.isNull(x)  ? 0 : x.anzahlTage;
    }
    
    $scope.saveAntrag = function() {
      
      var antragsDaten = {
          id : $scope.antrag.id,
          antragArt : $scope.antrag.antragArt.antragArt,
          sonderUrlaubArt : $scope.antrag.sonderUrlaubArt,
          von : parseDate($scope.antrag.von),
          bis : parseDate($scope.antrag.bis),
          anzahlTage: parseNumber($scope.antrag.anzahlTage)
      };
      antragService.update(antragsDaten, function(data) {
        $location.url("/antraege");
      });
    };
    
    $scope._addBewilliger = function(bewilligerKey) {
      $scope.bewilligungError="";
      var command = {
          "antragId": $scope.antrag.id,
          "benutzerId": bewilligerKey
      };
      bewilligungService.save(angular.toJson(command), function(data) {
        console.log(data);
        $scope.antrag.bewilligungen.push(data);
        $scope.bewilligerKey = "";
      },
      function(data) {
        console.log(data)
        $scope.bewilligungError=data.data.message;
      });
    }
    
    $scope.addBewilliger = function() {
       $scope._addBewilliger($scope.bewilligerKey);
    }
    
    $scope.deleteBewilligung = function(b) {
        bewilligungService.delete({"id" : b.id }, function(data) {
          $scope.antrag.bewilligungen = _.reject($scope.antrag.bewilligungen, function(x) {
            return _.isEqual(x, b);
          });
        });
    }
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

app.controller("ListBewilligungCtrl", function($scope, bewilligungService) {
  $scope.bewilligungsListe = bewilligungService.get({});
});
