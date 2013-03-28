// Encoding: UTF-8

var app = angular.module("antrag", [ "ngResource", "ui", "ui.bootstrap" ]);

app.config([ '$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
  $locationProvider.html5Mode(false);
  $locationProvider.hashPrefix("!");
  $routeProvider.when('/', {
    templateUrl : '/anwesenheit-web/resources/partials/home.html',
    controller : 'HomeCtrl'
  }).when('/antraege', {
    templateUrl : '/anwesenheit-web/resources/partials/antraege/index.html',
    controller : 'ListAntragCtrl'
  }).when("/antraege/new", {
    templateUrl : '/anwesenheit-web/resources/partials/antraege/new.html',
    controller : 'NewAntragCtrl',
  }).when("/uebersicht", {
    templateUrl : '/anwesenheit-web/resources/partials/antraege/uebersicht.html',
    controller : 'AntragUebersichtCtrl',
  }).when("/antraege/:id/edit", {
    templateUrl : '/anwesenheit-web/resources/partials/antraege/edit.html',
    controller : 'EditAntragCtrl',
  }).when("/antraege/:id", {
    templateUrl : '/anwesenheit-web/resources/partials/antraege/details.html',
    controller : 'AntragDetailsCtrl',
  }).when("/bewilligungen", {
    templateUrl : '/anwesenheit-web/resources/partials/bewilligungen/index.html',
    controller : 'ListBewilligungCtrl',
  });

} ]);

app.factory("antragService", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/antraege/:id", {
    "id" : "@id"
  }, {
    "update" : {
      "method" : "PUT"
    },
    "remove" : {
      "method" : "DELETE"
    }
  });
} ]);

app.factory("antragArtService", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/antragsarten/:id");
} ]);

app.factory("sonderUrlaubArtService", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/sonderurlaubarten/:id");
} ]);

app.factory("benutzerService", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/benutzer/:id");
} ]);

app.factory("bewilligungService", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/bewilligung/:id", {
    "id" : "@id"
  }, {
    "update" : {
      "method" : "PUT"
    },
    "remove" : {
      "method" : "DELETE"
    }
  });
} ]);

app.factory("bewilligungStatusService", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/bewilligungsstatus/:id");
} ]);

app.factory("antragUebersicht", [ '$resource', function($resource) {
  return $resource("/anwesenheit-web/api/antraege/uebersicht");
} ]);

app.controller("AppCtrl", [ '$rootScope', function($rootScope) {
  $rootScope.$on("$routeChangeError", function() {
    console.log("Error changing routes");
  });
} ]);

app.controller("HomeCtrl", [ '$scope', function($scope) {
} ]);

app.factory("antragListeData", function() {
  return {
    'filter' : {
      'status' : 'ALLE',
      'nur12Monate' : true
    }
  };
});

app.factory("bewilligungsListeData", function() {
  return {
    'filter' : {
      'benutzerName': '',
      'status' : ''
    }
  };
});

app.controller("ListAntragCtrl", [ '$scope', '$filter', '$dialog', 'antragService', 'antragListeData',
    function($scope, $filter, $dialog, antragService, antragListeData) {
      
      $scope.fetchAntragListe = function() {
        console.log("fetchAntragListe...");
        var d = new Date();
        d.setMonth(d.getMonth() - 12);
        var params = {};
        params.status = $scope.filter.status;
        if ($scope.filter.nur12Monate) {
          params.von = $filter("date")(d, "yyyy-MM-dd");
        }
        antragService.get(params, function(data) {
          $scope.antragListe = data;
        });
      };

      $scope.doDelete = function(antrag) {
        antragService.remove({ "id" : antrag.id }, function(data) {
          $scope.antragListe.antraege = _.reject($scope.antragListe.antraege, function(a) {
            return a.id === antrag.id;
          });
        });        
      };
      
      $scope.deleteAntrag = function(antrag) {
        var title = 'Antrag löschen?';
        var msg = 'Möchten Sie diesen Antrag wirklich löschen?';
        var btns = [{result:'cancel', label: 'Nein' }, {result:'ok', label: 'Ja', cssClass: 'btn-danger'}];

        $dialog.messageBox(title, msg, btns)
          .open()
          .then(function(result){
            if("ok" === result) {
              $scope.doDelete(antrag);
            }
        });
      };

      $scope.filter = antragListeData.filter;
      $scope.fetchAntragListe();
    } ]);

app.controller("AntragDetailsCtrl", [ '$scope', '$routeParams', 'antragService', function($scope, $routeParams, antragService) {
  $scope.antrag = antragService.get({
    "id" : $routeParams.id
  });

  $scope.sonderUrlaubArtVisible = function() {
    return $scope.antrag && $scope.antrag.antragArt && $scope.antrag.antragArt.antragArt === "SONDER";
  };
} ]);

function parseNumber(s) {
  var x = s.replace(/,/g, '.');
  return parseFloat(x);
}

app.controller("AntragUebersichtCtrl", [ '$scope', '$resource', 'antragUebersicht', function($scope, $resource, antragUebersicht) {
  $scope.antragListe = antragUebersicht.get({});
} ]);

app.controller("NewAntragCtrl", [ '$scope', '$location', '$filter', 'antragService', 'antragArtService', 'benutzerService',
    'sonderUrlaubArtService',
    function($scope, $location, $filter, antragService, antragArtService, benutzerService, sonderUrlaubArtService) {
      $scope.antragArtListe = antragArtService.query(function(liste) {
        $scope.antrag = {
          antragArt : _.clone(liste[0]),
          sonderUrlaubArt : "UMZUG",
          von : new Date(),
          bis : new Date(),
          anzahlTage : "0",
          bewilliger : []
        };
      });

      $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();

      $scope.createAntrag = function() {
        if ($scope.createForm.$valid) {
          var antragsDaten = {
            antragArt : $scope.antrag.antragArt.antragArt,
            sonderUrlaubArt : $scope.antrag.sonderUrlaubArt,
            von : $filter("date")($scope.antrag.von, "yyyy-MM-dd"),
            bis : $filter("date")($scope.antrag.bis, "yyyy-MM-dd"),
            anzahlTage : parseNumber($scope.antrag.anzahlTage),
            bewilliger : $.map($scope.antrag.bewilliger, function(b) {
              return b.benutzerId;
            })
          };

          antragService.save(angular.toJson(antragsDaten), function(data) {
            $location.path("/antraege");
          }, function(data) {
            console.log(data);
          });
        }
        ;
      };

      $scope.sonderUrlaubArtVisible = function() {
        return $scope.antrag && $scope.antrag.antragArt.antragArt === "SONDER";
      }

      $scope.anzahlTageVisible = function() {
        return !$scope.sonderUrlaubArtVisible();
      }

      $scope.getAnzahlTage = function() {
        if (!$scope.sonderUrlaubArtVisible())
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
        benutzerService.get({
          "id" : benutzerId
        }, function(benutzerDaten) {
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
          return _.isEqual(x, b);
        });
      };
    } ]);

app.controller("EditAntragCtrl", [
    '$scope',
    '$routeParams',
    '$filter',
    '$location',
    'antragArtService',
    'antragService',
    'benutzerService',
    'bewilligungService',
    'sonderUrlaubArtService',
    function($scope, $routeParams, $filter, $location, antragArtService, antragService, benutzerService, bewilligungService,
        sonderUrlaubArtService) {
      $scope.antragArtListe = antragArtService.query();
      $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();
      $scope.antrag = antragService.get({
        "id" : $routeParams.id
      }, function(data) {
        var s = _.isNull(data.sonderUrlaubArt) ? "UMZUG" : data.sonderUrlaubArt.sonderUrlaubArt;
        data.von = new Date(data.von), data.bis = new Date(data.bis), data.sonderUrlaubArt = s;
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
        if (!$scope.sonderUrlaubArtVisible())
          return 0;
        var x = _.find($scope.sonderUrlaubArtListe, function(a) {
          return _.isEqual(a.sonderUrlaubArt, $scope.antrag.sonderUrlaubArt);
        });
        return _.isNull(x) ? 0 : x.anzahlTage;
      }

      $scope.saveAntrag = function() {

        var antragsDaten = {
          id : $scope.antrag.id,
          antragArt : $scope.antrag.antragArt.antragArt,
          sonderUrlaubArt : $scope.antrag.sonderUrlaubArt,
          von : $filter("date")($scope.antrag.von, "yyyy-MM-dd"),
          bis : $filter("date")($scope.antrag.bis, "yyyy-MM-dd"),
          anzahlTage : parseNumber($scope.antrag.anzahlTage)
        };
        antragService.update(antragsDaten, function(data) {
          $location.path("/antraege");
        });
      };

      $scope._addBewilliger = function(bewilligerKey) {
        $scope.bewilligungError = "";
        var command = {
          "antragId" : $scope.antrag.id,
          "benutzerId" : bewilligerKey
        };
        bewilligungService.save(angular.toJson(command), function(data) {
          console.log(data);
          $scope.antrag.bewilligungen.push(data);
          $scope.bewilligerKey = "";
        }, function(data) {
          console.log(data)
          $scope.bewilligungError = data.data.message;
        });
      }

      $scope.addBewilliger = function() {
        $scope._addBewilliger($scope.bewilligerKey);
      }

      $scope.deleteBewilligung = function(b) {
        bewilligungService.remove({
          "id" : b.id
        }, function(data) {
          $scope.antrag.bewilligungen = _.reject($scope.antrag.bewilligungen, function(x) {
            return _.isEqual(x, b);
          });
        });
      }
    } ]);

app.directive("benutzerAutocomplete", function() {
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

app.controller("ListBewilligungCtrl", [ '$scope', 'bewilligungService', 'bewilligungsListeData',
    function($scope, bewilligungService, bewilligungsListeData) {
    
      $scope.fetchBewilligungsListe = function() {
        var params = {};
        if ($scope.filter.status) {
          params.status = $scope.filter.status
        }
        
        bewilligungService.get(params, function(data) { 
           $scope.bewilligungsListe = data; 
        });
      };

      $scope.bewilligeAntrag = function(b) {
        var updateCommand = {
          'id' : b.id,
          'bewilligungsStatus' : 'BEWILLIGT'
        };
        bewilligungService.update(updateCommand, function(data) {
          b.bewilligungsStatus = data.bewilligungsStatus;
        });
      };

      $scope.lehneAntragAb = function(b) {
        var updateCommand = {
          'id' : b.id,
          'bewilligungsStatus' : 'ABGELEHNT'
        };
        bewilligungService.update(updateCommand, function(data) {
          b.bewilligungsStatus = data.bewilligungsStatus;
        });
      };
      
      $scope.filter = bewilligungsListeData.filter;
      $scope.fetchBewilligungsListe();
    } 
]);
