app.controller('LogoutController',function($scope,$rootScope,$timeout,$http,$location,$cookies) {


        $http({method: 'POST', url: '/api/v1/authentication/logout', headers: {'Authorization': 'token '+$rootScope.token}}).then(function(response){
            console.log(response.data);
            $rootScope.token = null;
            $rootScope.userName = null;
            $cookies.remove("token");
            $cookies.remove("userName");
            $location.path("/login");
        }, function(error){
            $scope.error_message = error.data.message;
            $rootScope.token = null;
            $rootScope.userName = null;
            $cookies.remove("token");
            $cookies.remove("userName");
            $location.path("/login");
        });

    }
);

