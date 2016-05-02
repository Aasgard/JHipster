'use strict';

angular.module('opowerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('heater', {
                parent: 'entity',
                url: '/heaters',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Heaters'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/heater/heaters.html',
                        controller: 'HeaterController'
                    }
                },
                resolve: {
                }
            })
            .state('heater.detail', {
                parent: 'entity',
                url: '/heater/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Heater'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/heater/heater-detail.html',
                        controller: 'HeaterDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Heater', function($stateParams, Heater) {
                        return Heater.get({id : $stateParams.id});
                    }]
                }
            })
            .state('heater.new', {
                parent: 'heater',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/heater/heater-dialog.html',
                        controller: 'HeaterDialogController',
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
                        $state.go('heater', null, { reload: true });
                    }, function() {
                        $state.go('heater');
                    })
                }]
            })
            .state('heater.edit', {
                parent: 'heater',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/heater/heater-dialog.html',
                        controller: 'HeaterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Heater', function(Heater) {
                                return Heater.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('heater', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('heater.delete', {
                parent: 'heater',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/heater/heater-delete-dialog.html',
                        controller: 'HeaterDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Heater', function(Heater) {
                                return Heater.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('heater', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
