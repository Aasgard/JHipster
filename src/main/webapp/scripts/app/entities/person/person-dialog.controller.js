'use strict';

angular.module('opowerApp').controller('PersonDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Person', 'House',
        function($scope, $stateParams, $uibModalInstance, entity, Person, House) {

        $scope.person = entity;
        $scope.persons = Person.query();
        $scope.houses = House.query();
        $scope.load = function(id) {
            Person.get({id : id}, function(result) {
                $scope.person = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('opowerApp:personUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.person.id != null) {
                Person.update($scope.person, onSaveSuccess, onSaveError);
            } else {
                Person.save($scope.person, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
