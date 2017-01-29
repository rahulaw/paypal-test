app.controller('SignupController', function($scope,$rootScope,$timeout,$http,$location) {
        $scope.username = null;
        $scope.email = null;
        $scope.password = null;
        $scope.showErrors = false;

        $rootScope.token = null;
        $rootScope.userName = null;

        $scope.isSuccess = null;
        $scope.error_message = null;

        $scope.submit = function() {
            $scope.showErrors = true;

            if($scope.signup_form.$valid != true) {
                return;
            }

            var data = {"user_name" : $scope.username , "email" : $scope.email, "password" : $scope.password };
            $http.post('/api/v1/users', data).then(function(response){
                console.log(response.data);

                $scope.isSuccess = true;

                $rootScope.token = response.data.token;
                $rootScope.userName = response.data.userName;

                $location.path("/home");

            }, function(error){
                console.log(error.data);
                $scope.isSuccess = false;
                $scope.error_message = error.data.message;
            });

            console.log($scope.username);
            console.log($scope.email);
            console.log($scope.password);
        }
    }
);
