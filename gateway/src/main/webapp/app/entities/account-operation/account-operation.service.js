(function() {
    'use strict';
    angular
        .module('gatewayApp')
        .factory('AccountOperation', AccountOperation);

    AccountOperation.$inject = ['$resource', 'DateUtils'];

    function AccountOperation ($resource, DateUtils) {
        var resourceUrl =  'accountoperation/' + 'api/account-operations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
