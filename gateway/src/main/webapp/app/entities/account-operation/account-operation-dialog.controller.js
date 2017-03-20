(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('AccountOperationDialogController', AccountOperationDialogController);

    AccountOperationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AccountOperation'];

    function AccountOperationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AccountOperation) {
        var vm = this;

        vm.accountOperation = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountOperation.id !== null) {
                AccountOperation.update(vm.accountOperation, onSaveSuccess, onSaveError);
            } else {
                AccountOperation.save(vm.accountOperation, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('gatewayApp:accountOperationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
