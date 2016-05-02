'use strict';

angular.module('opowerApp').controller('ElectronicDeviceDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ElectronicDevice', 'House',
        function($scope, $stateParams, $uibModalInstance, entity, ElectronicDevice, House) {

        $scope.electronicDevice = entity;
        $scope.houses = House.query();
        $scope.load = function(id) {
            ElectronicDevice.get({id : id}, function(result) {
                $scope.electronicDevice = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opowerApp:electronicDeviceUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.electronicDevice.id != null) {
                ElectronicDevice.update($scope.electronicDevice, onSaveSuccess, onSaveError);
            } else {
                ElectronicDevice.save($scope.electronicDevice, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
