(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('account-operation', {
            parent: 'entity',
            url: '/account-operation?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.accountOperation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-operation/account-operations.html',
                    controller: 'AccountOperationController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountOperation');
                    $translatePartialLoader.addPart('accountOperationType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('account-operation-detail', {
            parent: 'entity',
            url: '/account-operation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.accountOperation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/account-operation/account-operation-detail.html',
                    controller: 'AccountOperationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountOperation');
                    $translatePartialLoader.addPart('accountOperationType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AccountOperation', function($stateParams, AccountOperation) {
                    return AccountOperation.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'account-operation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('account-operation-detail.edit', {
            parent: 'account-operation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-operation/account-operation-dialog.html',
                    controller: 'AccountOperationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountOperation', function(AccountOperation) {
                            return AccountOperation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-operation.new', {
            parent: 'account-operation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-operation/account-operation-dialog.html',
                    controller: 'AccountOperationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                accountId: null,
                                date: null,
                                type: null,
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('account-operation', null, { reload: 'account-operation' });
                }, function() {
                    $state.go('account-operation');
                });
            }]
        })
        .state('account-operation.edit', {
            parent: 'account-operation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-operation/account-operation-dialog.html',
                    controller: 'AccountOperationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccountOperation', function(AccountOperation) {
                            return AccountOperation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-operation', null, { reload: 'account-operation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('account-operation.delete', {
            parent: 'account-operation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/account-operation/account-operation-delete-dialog.html',
                    controller: 'AccountOperationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccountOperation', function(AccountOperation) {
                            return AccountOperation.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('account-operation', null, { reload: 'account-operation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
