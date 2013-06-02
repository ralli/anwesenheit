(function () {
  'use strict';

  /* global angular: false, $: false, _: false, toastr: false */

  var app = angular.module("app.ctrl.uebersicht", [ "ngResource", "ui", "ui.bootstrap", "app.services", "app.helpers"]);

  app.factory("antragUebersichtData", function () {
    return {
      "filter": {
        "antragsteller": "",
        "zeitFilter": "AKTUELL",
        "statusOffen": true,
        "statusBewilligt": true,
        "statusAbgelehnt": true,
        "statusStorniert": true
      }
    };
  });

  app.controller("AntragUebersichtCtrl", [ '$scope', '$resource', '$filter', 'antragUebersicht', "antragUebersichtData", "helpers",
    function ($scope, $resource, $filter, antragUebersicht, antragUebersichtData, helpers) {
      $scope.rowClassFor = helpers.rowClassForAntrag;
      $scope.fetchUebersicht = function () {
        var params = {};
        if ("AKTUELL" === $scope.filter.zeitFilter) {
          params.von = $filter("date")(new Date(), "yyyy-MM-dd");
          params.bis = params.von;
        } else if ("ZWOELF_MONATE" === $scope.filter.zeitFilter) {
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
        antragUebersicht.get(params, function (data) {
          $scope.antragListe = data;
        });
      };
      $scope.filter = antragUebersichtData.filter;
      $scope.fetchUebersicht();
    }
  ]);
}).call(this);