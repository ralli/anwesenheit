// Encoding: UTF-8

(function () {
  'use strict';

  /* global angular: false, $: false, _: false */

  var app = angular.module("app.services", [ "ngResource"]);

  app.factory("antragService", [ '$resource', function ($resource) {
    return $resource("./api/antraege/:id", { "id": "@id" },
        {
          "update": { "method": "PUT" },
          "remove": { "method": "DELETE" }
        });
  }]);

  app.factory("antragHistorieService", [ "$resource", function ($resource) {
    return $resource("./api/antraege/:antragId/historie", { "antragId": "@antragId" });
  }]);

  app.factory("antragArtService", [ '$resource', function ($resource) {
    return $resource("./api/antragsarten/:id");
  }]);

  app.factory("sonderUrlaubArtService", [ '$resource', function ($resource) {
    return $resource("./api/sonderurlaubarten/:id");
  }]);

  app.factory("benutzerService", [ '$resource', function ($resource) {
    return $resource("./api/benutzer/:id");
  }]);

  app.factory("bewilligungService", [ '$resource', function ($resource) {
    var result = $resource("./api/bewilligung/:id",
        { "id": "@id" },
        {
          "update": { "method": "PUT" },
          "remove": { "method": "DELETE" }
        });
    result.bewilligeAntrag = function (b, successCallback, errorCallback) {
      var updateCommand = {
        'id': b.id,
        'bewilligungsStatus': 'BEWILLIGT'
      };
      result.update(updateCommand,
          successCallback,
          errorCallback
      );
    };

    result.lehneAntragAb = function (b, successCallback, errorCallback) {
      var updateCommand = {
        'id': b.id,
        'bewilligungsStatus': 'ABGELEHNT'
      };
      result.update(updateCommand,
          successCallback,
          errorCallback
      );
    };

    return result;
  }]);

  app.factory("bewilligungStatusService", [ '$resource', function ($resource) {
    return $resource("./api/bewilligungsstatus/:id");
  }]);

  app.factory("antragUebersicht", [ '$resource', function ($resource) {
    return $resource("./api/antraege/uebersicht");
  }]);

  app.factory("arbeitstageService", [ '$resource', function ($resource) {
    return $resource("./api/arbeitstage/:id");
  }]);
}).call(this);