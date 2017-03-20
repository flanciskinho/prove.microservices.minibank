(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('AccountOperationDetailController', AccountOperationDetailController);

    AccountOperationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AccountOperation'];

    function AccountOperationDetailController($scope, $rootScope, $stateParams, previousState, entity, AccountOperation) {
        var vm = this;

        vm.accountOperation = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:accountOperationUpdate', function(event, result) {
            vm.accountOperation = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
