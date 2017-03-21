(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BankAccountWithdrawController', BankAccountWithdrawController);

    BankAccountWithdrawController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BankAccount'];

    function BankAccountWithdrawController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BankAccount) {
        var vm = this;

        vm.withdraw = entity;
        vm.clear = clear;
        vm.withdrawAmount = withdrawAmount;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function withdrawAmount () {
            vm.isSaving = true;
            BankAccount.withdraw(vm.withdraw, onAddSuccess, onAddError);
        }

        function onAddSuccess (result) {
            $scope.$emit('gatewayApp:bankAccountUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onAddError () {
            vm.isSaving = false;
        }


    }
})();
