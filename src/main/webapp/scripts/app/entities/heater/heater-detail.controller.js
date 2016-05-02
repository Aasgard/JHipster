'use strict';

angular.module('opowerApp')
    .controller('HeaterDetailController', function ($scope, $rootScope, $stateParams, entity, Heater, House) {
        $scope.heater = entity;
        $scope.load = function (id) {
            Heater.get({id: id}, function(result) {
                $scope.heater = result;
            });
        };
        var unsubscribe = $rootScope.$on('opowerApp:heaterUpdate', function(event, result) {
            $scope.heater = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
