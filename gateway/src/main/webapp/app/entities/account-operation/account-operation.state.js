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
                    value: 'date,desc',
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
        });
    }

})();
