'use strict';

angular.module('opowerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('house', {
                parent: 'entity',
                url: '/houses',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Houses'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/house/houses.html',
                        controller: 'HouseController'
                    }
                },
                resolve: {
                }
            })
            .state('house.detail', {
                parent: 'entity',
                url: '/house/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'House'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/house/house-detail.html',
                        controller: 'HouseDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'House', function($stateParams, House) {
                        return House.get({id : $stateParams.id});
                    }]
                }
            })
            .state('house.new', {
                parent: 'house',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/house/house-dialog.html',
                        controller: 'HouseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    squareMaters: null,
                                    nomberOfRooms: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('house', null, { reload: true });
                    }, function() {
                        $state.go('house');
                    })
                }]
            })
            .state('house.edit', {
                parent: 'house',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/house/house-dialog.html',
                        controller: 'HouseDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['House', function(House) {
                                return House.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('house', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('house.delete', {
                parent: 'house',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/house/house-delete-dialog.html',
                        controller: 'HouseDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['House', function(House) {
                                return House.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('house', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
