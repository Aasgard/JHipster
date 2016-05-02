'use strict';

angular.module('opowerApp')
    .controller('PersonController', function ($scope, $state, Person) {

        $scope.persons = [];
        $scope.loadAll = function() {
            Person.query(function(result) {
               $scope.persons = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.person = {
                firstName: null,
                lastName: null,
                email: null,
                id: null
            };
        };
    });
