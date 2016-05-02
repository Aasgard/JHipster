'use strict';

describe('Controller Tests', function() {

    describe('ElectronicDevice Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockElectronicDevice, MockHouse;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockElectronicDevice = jasmine.createSpy('MockElectronicDevice');
            MockHouse = jasmine.createSpy('MockHouse');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ElectronicDevice': MockElectronicDevice,
                'House': MockHouse
            };
            createController = function() {
                $injector.get('$controller')("ElectronicDeviceDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'opowerApp:electronicDeviceUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
