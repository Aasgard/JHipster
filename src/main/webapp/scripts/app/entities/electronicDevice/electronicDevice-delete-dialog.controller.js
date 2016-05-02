'use strict';

angular.module('opowerApp')
	.controller('ElectronicDeviceDeleteController', function($scope, $uibModalInstance, entity, ElectronicDevice) {

        $scope.electronicDevice = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ElectronicDevice.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
