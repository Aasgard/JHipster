'use strict';

angular.module('opowerApp').controller('HouseDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'House', 'Person', 'ElectronicDevice', 'Heater',
        function($scope, $stateParams, $uibModalInstance, entity, House, Person, ElectronicDevice, Heater) {

        $scope.house = entity;
        $scope.persons = Person.query();
        $scope.electronicdevices = ElectronicDevice.query();
        $scope.heaters = Heater.query();
        $scope.load = function(id) {
            House.get({id : id}, function(result) {
                $scope.house = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opowerApp:houseUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.house.id != null) {
                House.update($scope.house, onSaveSuccess, onSaveError);
            } else {
                House.save($scope.house, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
