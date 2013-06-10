// Encoding: UTF-8

(function () {
  'use strict';

  /* global angular: false, $: false, _: false, toastr: false */

  var app = angular.module("app.ctrl.reports", [ "ngResource", "ui", "ui.bootstrap", "app.services", "app.helpers"]);

  app.controller("ReportsCtrl", ["$scope", "$filter", function ($scope, $filter) {
    $scope.monate = [
      {"num": "01", "name": "Januar"},
      {"num": "02", "name": "Februar"},
      {"num": "03", "name": "März"},
      {"num": "04", "name": "April"},
      {"num": "05", "name": "Mai"},
      {"num": "06", "name": "Juni"},
      {"num": "07", "name": "Juli"},
      {"num": "08", "name": "August"},
      {"num": "09", "name": "September"},
      {"num": "10", "name": "Oktober"},
      {"num": "11", "name": "November"},
      {"num": "12", "name": "Dezember"}
    ];

    var d = new Date();
    d.setMonth(d.getMonth() - 1);
    $scope.monat = $filter("date")(d, "MM");
    $scope.jahr = d.getFullYear();
  }]);
}).call(this);