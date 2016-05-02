'use strict';

angular.module('opowerApp')
    .controller('ElectronicDeviceDetailController', function ($scope, $rootScope, $stateParams, entity, ElectronicDevice, House) {
        $scope.electronicDevice = entity;
        $scope.load = function (id) {
            ElectronicDevice.get({id: id}, function(result) {
                $scope.electronicDevice = result;
            });
        };
        var unsubscribe = $rootScope.$on('opowerApp:electronicDeviceUpdate', function(event, result) {
            $scope.electronicDevice = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
