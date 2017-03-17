(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BankAccountDetailController', BankAccountDetailController);

    BankAccountDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BankAccount'];

    function BankAccountDetailController($scope, $rootScope, $stateParams, previousState, entity, BankAccount) {
        var vm = this;

        vm.bankAccount = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:bankAccountUpdate', function(event, result) {
            vm.bankAccount = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
