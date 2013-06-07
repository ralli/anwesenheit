// Encoding: UTF-8

(function () {
  'use strict';

  /* global angular: false, $: false, _: false, toastr: false */

  var app = angular.module("app", [ "ngResource", "app.services", "app.ctrl.antrag", "app.ctrl.bewilligung", "app.ctrl.uebersicht", "app.ctrl.reports", "app.directives"]);

  app.config([ '$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {
    $locationProvider.html5Mode(false);
    $locationProvider.hashPrefix("!");

    $routeProvider.when('/', {
      templateUrl: './resources/partials/home.html',
      controller: 'HomeCtrl'
    }).when('/antraege', {
          templateUrl: './resources/partials/antraege/index.html',
          controller: 'ListAntragCtrl'
        }).when("/antraege/new", {
          templateUrl: './resources/partials/antraege/new.html',
          controller: 'NewAntragCtrl'
        }).when("/uebersicht", {
          templateUrl: './resources/partials/antraege/uebersicht.html',
          controller: 'AntragUebersichtCtrl'
        }).when("/uebersicht/:id", {
          templateUrl: './resources/partials/antraege/uebersichtdetails.html',
          controller: 'AntragDetailsCtrl'
        }).when("/antraege/:id/edit", {
          templateUrl: './resources/partials/antraege/edit.html',
          controller: 'EditAntragCtrl'
        }).when("/antraege/:id/copy", {
          templateUrl: './resources/partials/antraege/new.html',
          controller: 'NewAntragCtrl'
        }).when("/antraege/:id", {
          templateUrl: './resources/partials/antraege/details.html',
          controller: 'AntragDetailsCtrl'
        }).when("/bewilligungen", {
          templateUrl: './resources/partials/bewilligungen/index.html',
          controller: 'ListBewilligungCtrl'
        }).when("/bewilligungen/:id", {
            templateUrl: './resources/partials/bewilligungen/show.html',
            controller: 'ShowBewilligungCtrl'
        });

        $routeProvider.when("/reports", {
          templateUrl: './resources/partials/reports/index.html',
          controller: 'ReportsCtrl'
        });

    /*
     Set up an interceptor to watch for 401 errors.
     The server, rather than redirect to a login page (or whatever), just returns a 401 error
     if it receives a request that should have a user session going. Angular catches the error below
     and says what happens - in this case, we just redirect to a login page. You can get a little more
     complex with this strategy, such as queueing up failed requests and re-trying them once the user logs in.
     Read all about it here: http://www.espeo.pl/2012/02/26/authentication-in-angularjs-application
     */
    var interceptor = ['$q', '$location', '$rootScope', '$window', function ($q, $location, $rootScope, $window) {
      function success(response) {
        return response;
      }

      function error(response) {
        var status = response.status;
        if (status == 401) {
          $window.location.href = "/anwesenheit-web";
        }
        return $q.reject(response);
      }

      return function (promise) {
        return promise.then(success, error);
      }
    }];
    $httpProvider.responseInterceptors.push(interceptor);
  } ]);
}).call(this);
