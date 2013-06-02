// Encoding: UTF-8

(function () {
  'use strict';

  /* global angular: false, $: false, _: false, toastr: false */

  var app = angular.module("app.ctrl.bewilligung", [ "ngResource", "ui", "ui.bootstrap", "app.services", "app.helpers"]);

  app.controller("ListBewilligungCtrl", [ '$scope', 'bewilligungService', 'bewilligungsListeData', 'helpers',
    function ($scope, bewilligungService, bewilligungsListeData, helpers) {

      $scope.fetchBewilligungsListe = function () {
        var params = {};
        if ($scope.filter.status) {
          params.status = $scope.filter.status;
        }

        bewilligungService.get(params, function (data) {
          $scope.bewilligungsListe = data;
        });
      };

      $scope.bewilligeAntrag = function (b) {
        var successCallback = function (data) {
          b.bewilligungsStatus = data.bewilligungsStatus;
          toastr.success("Der Antrag wurde bewilligt");
        };
        var errorCallback = function (data) {
          toastr.error(data.message);
        };

        bewilligungService.bewilligeAntrag(b, successCallback, errorCallback);
      };

      $scope.lehneAntragAb = function (b) {
        var successCallback = function (data) {
          b.bewilligungsStatus = data.bewilligungsStatus;
          toastr.success("Der Antrag wurde abgelehnt");
        };

        var errorCallback = function (data) {
          toastr.error(data.message);
        };

        bewilligungService.lehneAntragAb(b, successCallback, errorCallback);
      };

      $scope.rowClassFor = helpers.rowClassForBewilligung;

      $scope.filter = bewilligungsListeData.filter;

      $scope.antragAenderbar = function (antrag) {
        return antrag.antragStatus.antragStatus === "NEU";
      };

      $scope.fetchBewilligungsListe();
    }
  ]);

  app.controller("ShowBewilligungCtrl", ['$scope', '$routeParams', '$location', 'bewilligungService', 'helpers',
    function ($scope, $routeParams, $location, bewilligungService, helpers) {
      $scope.bewilligung = bewilligungService.get({'id': $routeParams.id });

      $scope.sonderUrlaubVisible = function () {
        return $scope.bewilligung
            && $scope.bewilligung.antragArt
            && $scope.bewilligung.antragArt.antragArt === 'SONDER';
      };

      $scope.bewilligeAntrag = function (b) {
        var successCallback = function (/* data */) {
          $location.path("/bewilligungen");
          toastr.success("Der Antrag wurde bewilligt");
        };
        var errorCallback = function (data) {
          toastr.error(data.message);
        };

        bewilligungService.bewilligeAntrag(b, successCallback, errorCallback);
      };

      $scope.lehneAntragAb = function (b) {
        var successCallback = function (/* data */) {
          $location.path("/bewilligungen");
          toastr.success("Der Antrag wurde abgelehnt");
        };

        var errorCallback = function (data) {
          toastr.error(data.message);
        };

        bewilligungService.lehneAntragAb(b, successCallback, errorCallback);
      };

      $scope.hatGleichzeitigeAntraege = function () {
        return $scope.bewilligung
            && _.isArray($scope.bewilligung.gleichzeitigeAntraege)
            && $scope.bewilligung.gleichzeitigeAntraege.length > 0;
      };

      $scope.rowClassForAntrag = helpers.rowClassForAntrag;
      $scope.rowClassFor = helpers.rowClassForBewilligung;
      $scope.rolleForBewilliger = helpers.rolleForBewilliger;
    }
  ]);
}).call(this);