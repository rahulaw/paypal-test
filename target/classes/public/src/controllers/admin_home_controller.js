app.controller('AdminHomeController', function($scope,$rootScope,$timeout,$http,$location) {

        if ($rootScope.token == null || $rootScope.token == undefined) {
            $location.path("/login");
        }

        function getUsersList() {
            $http({method: 'GET', url: '/api/v1/users', headers: {'Authorization': 'token ' + $rootScope.token}})
                .then(function (response) {
                    console.log(response.data);
                    $scope.users = response.data;
                }, function (error) {
                    if (error.status == 401) {
                        $location.path("/login");
                    }
                });
        }

        getUsersList();
    }
);
