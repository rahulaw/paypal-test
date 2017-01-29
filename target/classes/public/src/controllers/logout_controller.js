app.controller('LogoutController',function($scope,$rootScope,$timeout,$http,$location) {

        $http({method: 'POST', url: '/api/v1/authentication/logout', headers: {'Authorization': 'token '+$rootScope.token}}).then(function(response){
            console.log(response.data);
            $rootScope.token = null;
            $location.path("/login");
        }, function(error){
            $scope.error_message = error.data.message;
            $location.path("/login");
        });

    }
);

