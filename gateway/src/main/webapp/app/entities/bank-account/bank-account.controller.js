(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BankAccountController', BankAccountController);

    BankAccountController.$inject = ['$scope', '$state', 'BankAccount', 'User', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants'];

    function BankAccountController ($scope, $state, BankAccount, User, ParseLinks, AlertService, pagingParams, paginationConstants) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            BankAccount.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.bankAccounts = data;
                vm.page = pagingParams.page;

                var arrayUser = vm.bankAccounts.map(function (obj){
                    return obj.userId;
                });
                getLogins(arrayUser);

            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function getLogins(arrayUserId) {
            // remove duplicates
            var tmp = arrayUserId.filter(function(item, pos) {
                return arrayUserId.indexOf(item) == pos;
            });

            User.login({id: tmp}, onSuccess, onError);
            function onSuccess(data, headers) {
                vm.loginProfiles = data;

                var aux;
                for (var cnt = 0; cnt < vm.bankAccounts.length; cnt++) {
                    // Get the specified loginProfile
                    aux = vm.loginProfiles.filter(function(item, pos) {
                        return item.id == this;
                    }, vm.bankAccounts[cnt].userId); // use as this when executing callback

                    vm.bankAccounts[cnt].login = (aux[0] === undefined) ? "" : aux[0].login;
                }
            }
            function onError() {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
