'use strict';

angular.module('opowerApp')
    .controller('HeaterController', function ($scope, $state, Heater) {

        $scope.heaters = [];
        $scope.loadAll = function() {
            Heater.query(function(result) {
               $scope.heaters = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.heater = {
                avgConsumption: null,
                id: null
            };
        };
    });
