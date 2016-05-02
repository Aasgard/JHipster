'use strict';

angular.module('opowerApp')
    .controller('HouseController', function ($scope, $state, House) {

        $scope.houses = [];
        $scope.loadAll = function() {
            House.query(function(result) {
               $scope.houses = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.house = {
                squareMaters: null,
                nomberOfRooms: null,
                id: null
            };
        };
    });
