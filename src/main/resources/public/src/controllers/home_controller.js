app.controller('HomeController', function($scope,$rootScope,$timeout,$http,$location) {

        if($rootScope.token == null || $rootScope.token == undefined) {
            $location.path("/login");
        }

        $scope.userName = $rootScope.userName;
        $scope.token = $rootScope.token;

    }
);
