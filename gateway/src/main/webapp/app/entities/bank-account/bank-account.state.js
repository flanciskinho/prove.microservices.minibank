(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bank-account', {
            parent: 'entity',
            url: '/bank-account?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.bankAccount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bank-account/bank-accounts.html',
                    controller: 'BankAccountController',
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
                    $translatePartialLoader.addPart('bankAccount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bank-account-detail', {
            parent: 'entity',
            url: '/bank-account/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'gatewayApp.bankAccount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bank-account/bank-account-detail.html',
                    controller: 'BankAccountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bankAccount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BankAccount', function($stateParams, BankAccount) {
                    return BankAccount.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bank-account',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        }).state('bank-account.new', {
            parent: 'bank-account',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank-account/bank-account-dialog.html',
                    controller: 'BankAccountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userId: null,
                                balance: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bank-account', null, { reload: 'bank-account' });
                }, function() {
                    $state.go('bank-account');
                });
            }]
        }).state('bank-account-detail.add', {
            parent: 'bank-account-detail',
            url: '/add',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank-account/bank-account-add.html',
                    controller: 'BankAccountAddController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['$stateParams', function($stateParams) {
                            return {
                                accountId : $stateParams.id,
                                amount: null
                            };
                        }]
                    }
                }).result.then(function() {
                    $state.go('bank-account', null, { reload: 'bank-account' });
                }, function() {
                    $state.go('bank-account-detail');
                });
            }]
        }).state('bank-account-detail.withdraw', {
            parent: 'bank-account-detail',
            url: '/withdraw',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bank-account/bank-account-withdraw.html',
                    controller: 'BankAccountWithdrawController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['$stateParams', function($stateParams) {
                            return {
                                accountId : $stateParams.id,
                                amount: null
                            };
                        }]
                    }
                }).result.then(function() {
                    $state.go('bank-account', null, { reload: 'bank-account' });
                }, function() {
                    $state.go('bank-account-detail');
                });
            }]
        });
    }

})();
