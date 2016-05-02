'use strict';

angular.module('opowerApp')
	.controller('HouseDeleteController', function($scope, $uibModalInstance, entity, House) {

        $scope.house = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            House.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
