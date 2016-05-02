'use strict';

angular.module('opowerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


