(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('AccountOperationDeleteController',AccountOperationDeleteController);

    AccountOperationDeleteController.$inject = ['$uibModalInstance', 'entity', 'AccountOperation'];

    function AccountOperationDeleteController($uibModalInstance, entity, AccountOperation) {
        var vm = this;

        vm.accountOperation = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AccountOperation.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
