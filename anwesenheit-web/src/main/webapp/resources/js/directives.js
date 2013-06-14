// Encoding: UTF-8

(function () {
  'use strict';

  /* global angular: false, $: false, _: false, toastr: false */

  var app = angular.module("app.directives", [ "ngResource", "ui", "ui.bootstrap", "app.services", "app.helpers"]);

  app.directive("antragdetails", ['antragHistorieService', 'helpers', function (antragHistorieService, helpers) {
    return {
      restrict: "EA",
      'scope': {
        'antrag': '=',
        'backlink': '@'
      },
      'templateUrl': './resources/partials/antragdetails.html',
      'link': function (scope) {
        scope.sonderUrlaubArtVisible = function () {
          return scope.antrag && scope.antrag.antragArt && scope.antrag.antragArt.antragArt === "SONDER";
        };

        scope.leseHistorie = function () {
          antragHistorieService.query({'antragId': scope.antrag.id }, function (data) {
            scope.historie = data;
          });
        };

        scope.rowClassFor = helpers.rowClassForBewilligung;
      }
    };
  }]);

  app.directive("markdown", function() {

     return {
       'restrict': 'A',
       'scope': {
         'ngContent': '='
       },
       'template': '<div ng-bind-html-unsafe="showMarkDown()"></div>',
       'link': function(scope) {
          scope.showMarkDown = function() {
            var converter = new Showdown.converter();
            var txt = scope.ngContent ? scope.ngContent : "";
            return converter.makeHtml(txt);
          };
       }
     };
  });

}).call(this);
