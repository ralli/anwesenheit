app = angular.module("bewilligung", ["ngResource"])

app.config(function($routeProvider) {
  $routeProvider.when('/', {
      templateUrl : 'index.html',
      controller : 'ListCtrl'
  });
});

app.factory("bewilligungService", function($resource) {
  return $resource("/anwesenheit-web/api/bewilligung/:id");
});

app.controller("ListCtrl", function($scope, bewilligungService) {
  $scope.bewilligungsListe = bewilligungService.get({});
});