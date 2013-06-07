// Encoding: UTF-8

(function () {
    'use strict';

    /* global angular: false, $: false, _: false, toastr: false */

    var app = angular.module("app.ctrl.reports", [ "ngResource", "ui", "ui.bootstrap", "app.services", "app.helpers"]);

    app.controller("ReportsCtrl", ["$scope", "$filter", function($scope, $filter) {
        $scope.monate = [
            {"num": "01", "name": "Januar"},
            {"num": "02", "name": "Februar"},
            {"num": "03", "name": "März"},
            {"num": "04", "name": "April"},
            {"num": "05", "name": "Mai"},
            {"num": "06", "name": "Juni"},
            {"num": "08", "name": "Juli"},
            {"num": "07", "name": "August"},
            {"num": "09", "name": "September"},
            {"num": "10", "name": "Oktober"},
            {"num": "11", "name": "November"},
            {"num": "12", "name": "Dezember"}
        ];

        var d = new Date();
        $scope.monat = $filter("date")(d, "MM");
        $scope.jahr = d.getFullYear();
        $scope.datumFun = function() {
            return new Date("" + $scope.jahr + "-" + $scope.monat + "-1");
        };
        $scope.datum = $scope.datumFun();
    }]);
}).call(this);