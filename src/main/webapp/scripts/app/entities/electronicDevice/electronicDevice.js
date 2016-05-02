'use strict';

angular.module('opowerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('electronicDevice', {
                parent: 'entity',
                url: '/electronicDevices',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ElectronicDevices'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/electronicDevice/electronicDevices.html',
                        controller: 'ElectronicDeviceController'
                    }
                },
                resolve: {
                }
            })
            .state('electronicDevice.detail', {
                parent: 'entity',
                url: '/electronicDevice/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ElectronicDevice'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/electronicDevice/electronicDevice-detail.html',
                        controller: 'ElectronicDeviceDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ElectronicDevice', function($stateParams, ElectronicDevice) {
                        return ElectronicDevice.get({id : $stateParams.id});
                    }]
                }
            })
            .state('electronicDevice.new', {
                parent: 'electronicDevice',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/electronicDevice/electronicDevice-dialog.html',
                        controller: 'ElectronicDeviceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    avgConsumption: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('electronicDevice', null, { reload: true });
                    }, function() {
                        $state.go('electronicDevice');
                    })
                }]
            })
            .state('electronicDevice.edit', {
                parent: 'electronicDevice',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/electronicDevice/electronicDevice-dialog.html',
                        controller: 'ElectronicDeviceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ElectronicDevice', function(ElectronicDevice) {
                                return ElectronicDevice.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('electronicDevice', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('electronicDevice.delete', {
                parent: 'electronicDevice',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/electronicDevice/electronicDevice-delete-dialog.html',
                        controller: 'ElectronicDeviceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ElectronicDevice', function(ElectronicDevice) {
                                return ElectronicDevice.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('electronicDevice', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
