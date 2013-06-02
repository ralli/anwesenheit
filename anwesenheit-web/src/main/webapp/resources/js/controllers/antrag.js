// Encoding: UTF-8

(function () {
  'use strict';

  /* global angular: false, $: false, _: false, toastr: false */

  var app = angular.module("app.ctrl.antrag", [ "ngResource", "ui", "ui.bootstrap", "app.services", "app.helpers"]);

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

  app.controller("ListAntragCtrl", [ '$scope', '$filter', '$dialog', '$http', 'antragService', 'antragListeData', 'helpers',
    function ($scope, $filter, $dialog, $http, antragService, antragListeData, helpers) {
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
        $http.put('./api/antraege/' + antrag.id + "/storno").success(function (/* data */) {
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

      $scope.rowClassFor = helpers.rowClassForAntrag;
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

  app.controller("AntragDetailsCtrl", [ '$scope', '$routeParams', 'antragService', 'antragHistorieService', 'helpers',
    function ($scope, $routeParams, antragService, antragHistorieService, helpers) {
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

      $scope.rowClassFor = helpers.rowClassForBewilligung;
    }
  ]);

  function parseNumber(s) {
    var x = s.replace(/,/g, '.');
    return parseFloat(x);
  }

  app.controller("NewAntragCtrl", [ '$scope',
    '$location',
    '$filter',
    '$routeParams',
    'antragService',
    'antragArtService',
    'benutzerService',
    'sonderUrlaubArtService',
    'arbeitstageService',
    'helpers',
    function ($scope, $location, $filter, $routeParams, antragService, antragArtService, benutzerService, sonderUrlaubArtService, arbeitstageService, helpers) {
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
        }, function (/* data */) {
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

          antragService.save(angular.toJson(antragsDaten), function (/*data*/) {
            $location.path("/antraege");
            toastr.success("Ihr Antrag wurde gespeichert");
          }, function (response) {
            toastr.error(response.data.message);
          });
        }
      };

      $scope.sonderUrlaubArtVisible = function () {
        return $scope.antrag
            && $scope.antrag.antragArt
            && $scope.antrag.antragArt.antragArt === "SONDER";
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
        var equalsBenutzerId = function (b) {
          return _.isEqual(b.benutzerId, benutzerId);
        };

        $scope.bewilligungError = "";

        var successCallback = function (benutzerDaten) {
          $scope.bewilligerKey = "";
          if (_.find($scope.antrag.bewilliger, equalsBenutzerId)) {
            $scope.bewilligungError = "Die Bewilligung kann nicht mehrfach hinzugefügt werden";
            return;
          }
          benutzerDaten.position = $scope.antrag.bewilliger.length + 1;
          $scope.antrag.bewilliger.push(benutzerDaten);
        };
        var errorCallback = function (data) {
          $scope.bewilligungError = data.data.message;
        };

        benutzerService.get({ "id": benutzerId }, successCallback, errorCallback);
      };

      $scope.addBewilliger = function () {
        if ($scope.bewilligungForm.$valid) {
          var benutzerId = $scope.bewilligerKey;
          $scope._addBewilliger(benutzerId);
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

      $scope.titleForBewilliger = helpers.rolleForBewilliger;
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
    'helpers',
    function ($scope, $routeParams, $filter, $location, antragArtService, antragService, benutzerService, bewilligungService, sonderUrlaubArtService, arbeitstageService, helpers) {
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
        return $scope.antrag
            && $scope.antrag.antragArt
            && $scope.antrag.antragArt.antragArt === "SONDER";
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
            function (/* data */) {
              $location.path("/antraege");
              toastr.success("Ihre Änderungen wurden gespeichert");
            },
            function (data) {
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
        }, function (/* data */) {
          $scope.antrag.bewilligungen = _.reject($scope.antrag.bewilligungen, function (x) {
            return _.isEqual(x, b);
          });
        });
      };

      $scope.rowClassFor = helpers.rowClassForBewilligung;

      $scope.aktualisiereArbeitstage = function () {
        var params = {};
        params.von = $filter("date")($scope.antrag.von, "yyyy-MM-dd");
        params.bis = $filter("date")($scope.antrag.bis, "yyyy-MM-dd");
        arbeitstageService.get(params, function (/* data */) {
          $scope.antrag.anzahlTage = $filter("number")(data.arbeitsTage);
        });
      };
    }
  ]);

  app.directive("benutzerAutocomplete", function () {
    return {
      restrict: 'A',
      link: function (scope, iElement /*, attr, ctrl */) {
        iElement.autocomplete({
          'source': "./api/benutzer/search",
          'select': function (event, ui) {
            scope.bewilligerKey = ui.item.value;
            // Eigentlich wäre es cooler, wenn hier sofort ein Bewilliger hinzugefügt würde,
            // leider scheint das aktuell nicht richtig zu funktionieren...
            // scope._addBewilliger(ui.item.value);
          },
          'minLength': 2
        });
      }
    };
  });

}).call(this);
