'use strict';

angular.module('opowerApp').controller('HeaterDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Heater', 'House',
        function($scope, $stateParams, $uibModalInstance, entity, Heater, House) {

        $scope.heater = entity;
        $scope.houses = House.query();
        $scope.load = function(id) {
            Heater.get({id : id}, function(result) {
                $scope.heater = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opowerApp:heaterUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.heater.id != null) {
                Heater.update($scope.heater, onSaveSuccess, onSaveError);
            } else {
                Heater.save($scope.heater, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
