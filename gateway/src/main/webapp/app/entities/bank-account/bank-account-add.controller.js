(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BankAccountAddController', BankAccountAddController);

    BankAccountAddController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BankAccount'];

    function BankAccountAddController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BankAccount) {
        var vm = this;

        vm.add = entity;
        vm.clear = clear;
        vm.addAmount = addAmount;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function addAmount () {
            vm.isSaving = true;
            BankAccount.add(vm.add, onAddSuccess, onAddError);
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
