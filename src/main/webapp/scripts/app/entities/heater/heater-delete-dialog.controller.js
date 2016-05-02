'use strict';

angular.module('opowerApp')
	.controller('HeaterDeleteController', function($scope, $uibModalInstance, entity, Heater) {

        $scope.heater = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Heater.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
