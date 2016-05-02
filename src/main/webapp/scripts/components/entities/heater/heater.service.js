'use strict';

angular.module('opowerApp')
    .factory('Heater', function ($resource, DateUtils) {
        return $resource('api/heaters/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
