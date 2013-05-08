// Encoding: UTF-8

'use strict';

/* global angular: false, $: false, _: false, toastr: false */

var app = angular.module("antrag", [ "ngResource", "ui", "ui.bootstrap" ]);

function rowClassForAntrag(antrag) {
    var status = antrag.antragStatus.antragStatus,
        result = "";

    if (status === 'NEU') {
        result = '';
    } else if (status === 'IN_ARBEIT') {
        result = 'info';
    } else if (status === 'BEWILLIGT') {
        result = 'success';
    } else if (status === 'ABGELEHNT') {
        result = 'error';
    } else if (status === 'STORNIERT') {
        result = 'storniert';
    }

    return result;
}

function rowClassForBewilligung(b) {
    var status = b.bewilligungsStatus.bewilligungsStatus,
        result = "";

    if (status === 'ABGELEHNT') {
        result = "error";
    } else if (status === 'OFFEN') {
        result = "";
    } else if (status === 'BEWILLIGT') {
        result = "success";
    }

    return result;
}

function rolleForBewilliger(b) {
    if(b.position === 1) {
      return "1. Unterschrift";
    } else if(b.position === 2) {
      return "2. Unterschrift";
    }
    else {
      return "Zur Info";
    }
 }

app.config([ '$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(false);
    $locationProvider.hashPrefix("!");
    
    $routeProvider.when('/', {
        templateUrl: '/anwesenheit-web/resources/partials/home.html',
        controller: 'HomeCtrl'
    }).when('/antraege', {
        templateUrl: '/anwesenheit-web/resources/partials/antraege/index.html',
        controller: 'ListAntragCtrl'
    }).when("/antraege/new", {
        templateUrl: '/anwesenheit-web/resources/partials/antraege/new.html',
        controller: 'NewAntragCtrl'
    }).when("/uebersicht", {
        templateUrl: '/anwesenheit-web/resources/partials/antraege/uebersicht.html',
        controller: 'AntragUebersichtCtrl'
    }).when("/antraege/:id/edit", {
        templateUrl: '/anwesenheit-web/resources/partials/antraege/edit.html',
        controller: 'EditAntragCtrl'
    }).when("/antraege/:id/copy", {
        templateUrl: '/anwesenheit-web/resources/partials/antraege/new.html',
        controller: 'NewAntragCtrl'
    }).when("/antraege/:id", {
        templateUrl: '/anwesenheit-web/resources/partials/antraege/details.html',
        controller: 'AntragDetailsCtrl'
    }).when("/bewilligungen", {
        templateUrl: '/anwesenheit-web/resources/partials/bewilligungen/index.html',
        controller: 'ListBewilligungCtrl'
    }).when("/bewilligungen/:id", {
        templateUrl: '/anwesenheit-web/resources/partials/bewilligungen/show.html',
        controller: 'ShowBewilligungCtrl'
    });
} ]);

app.factory("antragService", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/antraege/:id", { "id": "@id" },
        {
            "update": { "method": "PUT" },
            "remove": { "method": "DELETE" }
        });
}]);

app.factory("antragHistorieService", [ "$resource", function ($resource) {
    return $resource("/anwesenheit-web/api/antraege/:antragId/historie", { "antragId": "@antragId" });
}]);

app.factory("antragArtService", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/antragsarten/:id");
}]);

app.factory("sonderUrlaubArtService", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/sonderurlaubarten/:id");
}]);

app.factory("benutzerService", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/benutzer/:id");
}]);

app.factory("bewilligungService", [ '$resource', function ($resource) {
    var result = $resource("/anwesenheit-web/api/bewilligung/:id",
        { "id": "@id" },
        {
            "update": { "method": "PUT" },
            "remove": { "method": "DELETE" }
        });
    result.bewilligeAntrag = function (b, successCallback) {
        var updateCommand = {
            'id': b.id,
            'bewilligungsStatus': 'BEWILLIGT'
        };
        result.update(updateCommand,
            successCallback,
            function(data) {
                toastr.error(data.message);
            }
        );
      };

      result.lehneAntragAb = function (b, successCallback) {
        var updateCommand = {
            'id': b.id,
            'bewilligungsStatus': 'ABGELEHNT'
        };
        result.update(updateCommand,
          successCallback,
          function(data) {
            toastr.error(data.message);
          }
        );
      };

    return result;
}]);

app.factory("bewilligungStatusService", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/bewilligungsstatus/:id");
}]);

app.factory("antragUebersicht", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/antraege/uebersicht");
}]);

app.factory("arbeitstageService", [ '$resource', function ($resource) {
    return $resource("/anwesenheit-web/api/arbeitstage/:id");
}]);

app.controller("AppCtrl", [ '$rootScope', function ($rootScope) {
    $rootScope.$on("$routeChangeError", function () {
        // console.log("Error changing routes");
    });
    $rootScope.alerts = { type: 'success', message: "Hurra"};
}]);

app.controller("HomeCtrl", [ '$scope', function ($scope) {
}]);

app.factory("antragListeData", function () {
    return {
        'filter': {
            'status': 'ALLE',
            'nur12Monate': true
        }
    };
});

app.factory("bewilligungsListeData", function () {
    return {
        'filter': {
            'benutzerName': '',
            'status': ''
        }
    };
});

app.factory("antragUebersichtData", function() {
    return {
        "filter": {
            "zeitFilter": "AKTUELL",
            "statusOffen": true,
            "statusBewilligt": true,
            "statusAbgelehnt": true,
            "statusStorniert": true
        }
    };
});

app.controller("ListAntragCtrl", [ '$scope', '$filter', '$dialog', '$http', 'antragService', 'antragListeData',
    function ($scope, $filter, $dialog, $http, antragService, antragListeData) {
        $scope.fetchAntragListe = function () {
            var d = new Date(),
                params = {};

            d.setMonth(d.getMonth() - 12);
            params.status = $scope.filter.status;

            if ($scope.filter.nur12Monate) {
                params.von = $filter("date")(d, "yyyy-MM-dd");
            }

            antragService.get(params, function (data) {
                $scope.antragListe = data;
            });
        };

        $scope.doDelete = function (antrag) {
            antragService.remove({ "id": antrag.id },
                function (/* data */) {
                    $scope.antragListe.antraege = _.reject($scope.antragListe.antraege, function (a) {
                        return a.id === antrag.id;
                    });
                    toastr.success("Ihr Antrag wurde gelöscht");
                },
                function (data) {
                    toastr.error(data.message);
                }
            );
        };

        $scope.doStorno = function (antrag) {
            $http.put('/anwesenheit-web/api/antraege/' + antrag.id + "/storno").success(function (data) {
                //noinspection JSPrimitiveTypeWrapperUsage
                antrag.antragStatus.antragStatus = "STORNIERT";
                //noinspection JSPrimitiveTypeWrapperUsage
                antrag.antragStatus.bezeichnung = "Storniert";
                toastr.success("Ihr Antrag wurde storniert");
            }).error(function (data) {
                    toastr.error(data.message);
                });
        };

        $scope.deleteAntrag = function (antrag) {
            var title = 'Antrag löschen?',
                msg = 'Möchten Sie diesen Antrag wirklich löschen?',
                btns = [
                    {result: 'cancel', label: 'Nein' },
                    {result: 'ok', label: 'Ja', cssClass: 'btn-danger'}
                ];

            $dialog.messageBox(title, msg, btns)
                .open()
                .then(function (result) {
                    if ("ok" === result) {
                        $scope.doDelete(antrag);
                    }
                });
        };

        $scope.storniereAntrag = function (antrag) {
            var title = 'Antrag stornieren?',
                msg = 'Möchten Sie diesen Antrag wirklich stornieren?',
                btns = [
                    {result: 'cancel', label: 'Nein' },
                    {result: 'ok', label: 'Ja', cssClass: 'btn-danger'}
                ];

            $dialog.messageBox(title, msg, btns)
                .open()
                .then(function (result) {
                    if ("ok" === result) {
                        $scope.doStorno(antrag);
                    }
                });
        };

        $scope.rowClassFor = rowClassForAntrag;
        $scope.antragAenderbar = function (antrag) {
            return antrag.antragStatus.antragStatus === "NEU";
        };
        $scope.antragStornierbar = function (antrag) {
            return !($scope.antragAenderbar(antrag) || (antrag.antragStatus.antragStatus === "STORNIERT"));
        };
        $scope.antragKopierbar = function (antrag) {
            return !$scope.antragAenderbar(antrag);
        };
        $scope.filter = antragListeData.filter;
        $scope.fetchAntragListe();
    }]);

app.controller("AntragDetailsCtrl", [ '$scope', '$routeParams', 'antragService', 'antragHistorieService', function ($scope, $routeParams, antragService, antragHistorieService) {
    $scope.antrag = antragService.get({
        "id": $routeParams.id
    });

    $scope.sonderUrlaubArtVisible = function () {
        return $scope.antrag && $scope.antrag.antragArt && $scope.antrag.antragArt.antragArt === "SONDER";
    };

    $scope.leseHistorie = function () {
        antragHistorieService.query({'antragId': $scope.antrag.id }, function (data) {
            $scope.historie = data;
        });
    };

    $scope.rowClassFor = rowClassForBewilligung;
}]);

function parseNumber(s) {
    var x = s.replace(/,/g, '.');
    return parseFloat(x);
}

app.controller("AntragUebersichtCtrl", [ '$scope', '$resource', '$filter', 'antragUebersicht', "antragUebersichtData", function ($scope, $resource, $filter, antragUebersicht, antragUebersichtData) {
    $scope.rowClassFor = rowClassForAntrag;
    $scope.fetchUebersicht = function() {
        var params = {};
        if("AKTUELL" === $scope.filter.zeitFilter) {
            params.von = $filter("date")(new Date(), "yyyy-MM-dd");
            params.bis = params.von;
        } else if("ZWOELF_MONATE" === $scope.filter.zeitFilter) {
            var von = new Date();
            von.setMonth(von.getMonth() - 12);
            params.von = $filter("date")(von, "yyyy-MM-dd");
        }
        else {
            /* alle anzeigen */
        }
        params.statusOffen = $scope.filter.statusOffen;
        params.statusBewilligt = $scope.filter.statusBewilligt;
        params.statusAbgelehnt = $scope.filter.statusAbgelehnt;
        params.statusStorniert = $scope.filter.statusStorniert;
        antragUebersicht.get(params, function(data) {
            $scope.antragListe = data;
        });
    };
    $scope.filter = antragUebersichtData.filter;
    $scope.fetchUebersicht();
} ]);

app.controller("NewAntragCtrl", [ '$scope',
    '$location',
    '$filter',
    '$routeParams',
    'antragService',
    'antragArtService',
    'benutzerService',
    'sonderUrlaubArtService',
    'arbeitstageService',
    function ($scope, $location, $filter, $routeParams, antragService, antragArtService, benutzerService, sonderUrlaubArtService, arbeitstageService) {
        if ($routeParams.id) {
            /*
             * routeParams.id != null ==> Bestehender Antrag soll kopiert werden...
             */
            $scope.title = "Antrag kopieren";
            $scope.antragArtListe = antragArtService.query();
            $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();
            $scope.antrag = antragService.get({"id": $routeParams.id }, function (data) {
                var s = _.isNull(data.sonderUrlaubArt) ? "UMZUG" : data.sonderUrlaubArt.sonderUrlaubArt;
                data.von = new Date(data.von);
                data.bis = new Date(data.bis);
                data.sonderUrlaubArt = s;
                data.anzahlTage = $filter("number")(data.anzahlTage);
                data.bewilliger = _.map(data.bewilligungen, function (x) {
                    return x.benutzer;
                });
                data.bewilligungen = [];
            }, function (data) {
                toastr.error("Fehler beim Lesen der Daten");
            });
        } else {
            /*
             * routeParams.id === null ==> Neuer Antrag soll angelegt werden
             */
            $scope.title = "Neuer Antrag";
            $scope.antragArtListe = antragArtService.query(function (liste) {
                $scope.antrag = {
                    antragArt: _.clone(liste[0]),
                    sonderUrlaubArt: "UMZUG",
                    von: new Date(),
                    bis: new Date(),
                    anzahlTage: "0",
                    bewilliger: []
                };
            });

            $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();
        }

        $scope.createAntrag = function () {
            if ($scope.createForm.$valid) {
                var antragsDaten = {
                    antragArt: $scope.antrag.antragArt.antragArt,
                    sonderUrlaubArt: $scope.antrag.sonderUrlaubArt,
                    von: $filter("date")($scope.antrag.von, "yyyy-MM-dd"),
                    bis: $filter("date")($scope.antrag.bis, "yyyy-MM-dd"),
                    anzahlTage: parseNumber($scope.antrag.anzahlTage),
                    bewilliger: $.map($scope.antrag.bewilliger, function (b) {
                        return b.benutzerId;
                    })
                };

                antragService.save(angular.toJson(antragsDaten), function (data) {
                    $location.path("/antraege");
                    toastr.success("Ihr Antrag wurde gespeichert");
                }, function (response) {
                    toastr.error(response.data.message);
                });
            }
        };

        $scope.sonderUrlaubArtVisible = function () {
            return $scope.antrag && $scope.antrag.antragArt && $scope.antrag.antragArt.antragArt === "SONDER";
        };

        $scope.anzahlTageVisible = function () {
            return !$scope.sonderUrlaubArtVisible();
        };

        $scope.getAnzahlTage = function () {
            if (!$scope.sonderUrlaubArtVisible())
                return 0;
            var x = _.find($scope.sonderUrlaubArtListe, function (a) {
                return _.isEqual(a.sonderUrlaubArt, $scope.antrag.sonderUrlaubArt);
            });
            return x.anzahlTage;
        };

        $scope.controlClassFor = function (flag) {
            var result;
            if (flag) {
                result = "control-group";
            } else {
                result = "control-group error";
            }
            return result;
        };

        $scope._addBewilliger = function (benutzerId) {
            $scope.bewilligungError = "";
            if (_.find($scope.antrag.bewilliger, function (b) {
                return _.isEqual(b.benutzerId, benutzerId);
            })) {
                $scope.bewilligungError = "Die Bewilligung kann nicht mehrfach hinzugefügt werden";
                return;
            }
            benutzerService.get({
                "id": benutzerId
            }, function (benutzerDaten) {
                benutzerDaten.position = $scope.antrag.bewilliger.length + 1;
                $scope.antrag.bewilliger.push(benutzerDaten);
                $scope.bewilligerKey = "";
            }, function (data) {
                $scope.bewilligungError = data.data.message;
            });
        };

        $scope.addBewilliger = function () {
            if ($scope.bewilligungForm.$valid) {
                var benutzerId = $scope.bewilligerKey;
                return $scope._addBewilliger(benutzerId);
            }
        };

        $scope.benutzerSelected = function (ui) {
            $scope._addBewilliger(ui.value);
        };

        $scope.removeBewilliger = function (b) {
            $scope.antrag.bewilliger = _.reject($scope.antrag.bewilliger, function (x) {
                return _.isEqual(x, b);
            });
        };

        $scope.aktualisiereArbeitstage = function () {
            var params = {};
            params.von = $filter("date")($scope.antrag.von, "yyyy-MM-dd");
            params.bis = $filter("date")($scope.antrag.bis, "yyyy-MM-dd");
            arbeitstageService.get(params, function (data) {
                $scope.antrag.anzahlTage = $filter("number")(data.arbeitsTage);
            });
        };

        $scope.titleForBewilliger = rolleForBewilliger;
    }]);

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
    'arbeitstageService',
    function ($scope, $routeParams, $filter, $location, antragArtService, antragService, benutzerService, bewilligungService, sonderUrlaubArtService, arbeitstageService) {
        $scope.antragArtListe = antragArtService.query();
        $scope.sonderUrlaubArtListe = sonderUrlaubArtService.query();
        $scope.antrag = antragService.get({
            "id": $routeParams.id
        }, function (data) {
            var s = _.isNull(data.sonderUrlaubArt) ? "UMZUG" : data.sonderUrlaubArt.sonderUrlaubArt;
            data.von = new Date(data.von);
            data.bis = new Date(data.bis);
            data.sonderUrlaubArt = s;
            data.anzahlTage = $filter("number")(data.anzahlTage);
        }, function (data) {
            toastr.error(data.message);
        });

        $scope.controlClassFor = function (flag) {
            var result;
            if (flag) {
                result = "control-group";
            } else {
                result = "control-group error";
            }
            return result;
        };

        $scope.sonderUrlaubArtVisible = function () {
            return $scope.antrag && $scope.antrag.antragArt && $scope.antrag.antragArt.antragArt === "SONDER";
        };

        $scope.anzahlTageVisible = function () {
            return !$scope.sonderUrlaubArtVisible();
        };

        $scope.getAnzahlTage = function () {
            if (!$scope.sonderUrlaubArtVisible())
                return 0;
            var x = _.find($scope.sonderUrlaubArtListe, function (a) {
                return _.isEqual(a.sonderUrlaubArt, $scope.antrag.sonderUrlaubArt);
            });
            return _.isNull(x) ? 0 : x.anzahlTage;
        };

        $scope.saveAntrag = function () {
            var antragsDaten = {
                id: $scope.antrag.id,
                antragArt: $scope.antrag.antragArt.antragArt,
                sonderUrlaubArt: $scope.antrag.sonderUrlaubArt,
                von: $filter("date")($scope.antrag.von, "yyyy-MM-dd"),
                bis: $filter("date")($scope.antrag.bis, "yyyy-MM-dd"),
                anzahlTage: parseNumber($scope.antrag.anzahlTage)
            };

            antragService.update(antragsDaten,
              function (data) {
                $location.path("/antraege");
                toastr.success("Ihre Änderungen wurden gespeichert");
              },
              function(data) {
                toastr.error(data.message);
              }
            );
        };

        $scope._addBewilliger = function (bewilligerKey) {
            $scope.bewilligungError = "";
            var command = {
                "antragId": $scope.antrag.id,
                "benutzerId": bewilligerKey
            };
            bewilligungService.save(angular.toJson(command), function (data) {
                $scope.antrag.bewilligungen.push(data);
                $scope.bewilligerKey = "";
            }, function (data) {
                $scope.bewilligungError = data.data.message;
            });
        };

        $scope.addBewilliger = function () {
            $scope._addBewilliger($scope.bewilligerKey);
        };

        $scope.deleteBewilligung = function (b) {
            bewilligungService.remove({
                "id": b.id
            }, function (data) {
                $scope.antrag.bewilligungen = _.reject($scope.antrag.bewilligungen, function (x) {
                    return _.isEqual(x, b);
                });
            });
        };

        $scope.rowClassFor = rowClassForBewilligung;

        $scope.aktualisiereArbeitstage = function () {
            var params = {};
            params.von = $filter("date")($scope.antrag.von, "yyyy-MM-dd");
            params.bis = $filter("date")($scope.antrag.bis, "yyyy-MM-dd");
            arbeitstageService.get(params, function (data) {
                $scope.antrag.anzahlTage = $filter("number")(data.arbeitsTage);
            });
        };
    }
]);

app.directive("benutzerAutocomplete", function () {
    return {
        restrict: 'A',
        link: function (scope, iElement, attr, ctrl) {
            iElement.autocomplete({
                source: "/anwesenheit-web/api/benutzer/search",
                select: function (event, ui) {
                    scope.bewilligerKey = ui.item.value;
                    scope.$digest();
                    scope._addBewilliger(ui.item.value);
                },
                minLength: 2
            });
        }
    };
});

app.controller("ListBewilligungCtrl", [ '$scope', 'bewilligungService', 'bewilligungsListeData',
    function ($scope, bewilligungService, bewilligungsListeData) {

        $scope.fetchBewilligungsListe = function () {
            var params = {};
            if ($scope.filter.status) {
                params.status = $scope.filter.status;
            }

            bewilligungService.get(params, function (data) {
                $scope.bewilligungsListe = data;
            });
        };

        $scope.bewilligeAntrag = function(b) {
            bewilligungService.bewilligeAntrag(b, function(data) {
                b.bewilligungsStatus = data.bewilligungsStatus;
                toastr.success("Der Antrag wurde bewilligt");
            });
        };

        $scope.lehneAntragAb = function(b) {
            bewilligungService.lehneAntragAb(b, function(data) {
                    b.bewilligungsStatus = data.bewilligungsStatus;
                    toastr.success("Der Antrag wurde abgelehnt");
            });
        };

        $scope.rowClassFor = rowClassForBewilligung;

        $scope.filter = bewilligungsListeData.filter;

        $scope.antragAenderbar = function (antrag) {
            return antrag.antragStatus.antragStatus === "NEU";
        };

        $scope.fetchBewilligungsListe();
    }
]);

app.controller("ShowBewilligungCtrl", ['$scope', '$routeParams', '$location', 'bewilligungService', function($scope, $routeParams, $location, bewilligungService) {
	$scope.bewilligung = bewilligungService.get({'id': $routeParams.id });
	$scope.sonderUrlaubVisible = function() {
		return $scope.bewilligung !== null && $scope.bewilligung.antragArt !== null && $scope.bewilligung.antragArt.antragArt === 'SONDER';
	};

    $scope.bewilligeAntrag = function(b) {
        bewilligungService.bewilligeAntrag(b, function(data) {
            $location.path("/bewilligungen");
            toastr.success("Der Antrag wurde bewilligt");
        });
    };

    $scope.lehneAntragAb = function(b) {
        bewilligungService.lehneAntragAb(b, function(data) {
            $location.path("/bewilligungen");
            toastr.success("Der Antrag wurde abgelehnt");
        });
    };

    $scope.hatGleichzeitigeAntraege = function() {
        return $scope.bewilligung.gleichzeitigeAntraege !== null && $scope.bewilligung.gleichzeitigeAntraege.length > 0;
    };

    $scope.rowClassForAntrag = rowClassForAntrag;
    $scope.rowClassFor = rowClassForBewilligung;
    $scope.rolleForBewilliger = rolleForBewilliger;
}]);
