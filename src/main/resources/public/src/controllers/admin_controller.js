app.controller('AdminController', function($scope,$rootScope,$timeout,$http,$location) {

        $scope.username = null;
        $scope.password = null;
        $scope.showErrors = false;

        $scope.isSuccess = null;
        $scope.error_message = null;


        $rootScope.token = null;
        $rootScope.userName = null;

        $scope.submit = function() {
            $scope.showErrors = true;

            if($scope.admin_form.$valid != true) {
                return;
            }

            if($scope.username != 'admin') {
                alert("only admin users are allowed to login from this page");
                return;
            }

            var data = {"user_name" : $scope.username , "password" : $scope.password };
            $http.post('/api/v1/authentication/login', data).then(function(response){
                $scope.isSuccess = true;

                console.log(response.data);

                $rootScope.token = response.data.token;
                $rootScope.userName = $scope.username;

                $location.path("/admin_home");

            }, function(error){
                $scope.isSuccess = false;
                console.log(error.data);
                $scope.error_message = error.data.message;
            });

        }
    }
);
