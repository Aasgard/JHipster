'use strict';

angular.module('opowerApp')
    .controller('HouseDetailController', function ($scope, $rootScope, $stateParams, entity, House, Person, ElectronicDevice, Heater) {
        $scope.house = entity;
        $scope.load = function (id) {
            House.get({id: id}, function(result) {
                $scope.house = result;
            });
        };
        var unsubscribe = $rootScope.$on('opowerApp:houseUpdate', function(event, result) {
            $scope.house = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
