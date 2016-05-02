'use strict';

angular.module('opowerApp')
    .controller('ElectronicDeviceController', function ($scope, $state, ElectronicDevice) {

        $scope.electronicDevices = [];
        $scope.loadAll = function() {
            ElectronicDevice.query(function(result) {
               $scope.electronicDevices = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.electronicDevice = {
                avgConsumption: null,
                id: null
            };
        };
    });
