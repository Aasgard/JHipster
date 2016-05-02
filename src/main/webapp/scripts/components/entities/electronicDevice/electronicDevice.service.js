'use strict';

angular.module('opowerApp')
    .factory('ElectronicDevice', function ($resource, DateUtils) {
        return $resource('api/electronicDevices/:id', {}, {
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
