(function() {
  var app = angular.module("app", []);

  app.config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(false);
    $locationProvider.hashPrefix("!");

    $routeProvider.when('/', {
      templateUrl: './resources/partials/home.html',
      controller: 'HomeCtrl'
    });

    $routeProvider.when('/benutzer', {
      templateUrl: './resources/partials/benutzer/index.html',
      controller: 'BenutzerListCtrl'
    });

    $routeProvider.when('/parameter', {
      templateUrl: './resources/partials/parameter/index.html',
      controller: 'ParameterListCtrl'
    });
  }]);

  app.controller("HomeCtrl", ['$scope', function($scope) {
    $scope.message = "Hallo";
  }]);

  app.controller("BenutzerListCtrl", ["$scope", function($scope) {
  }]);

  app.controller("ParameterListCtrl", ["$scope", function($scope) {

  }]);
}).call(this);
