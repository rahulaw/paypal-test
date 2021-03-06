app.controller('PaymentController', function($scope,$rootScope,$timeout,$http,$location) {

        $scope.email = null;
        $scope.amount = null;
        $scope.error_message = null;
        $scope.isSuccess = null;

        if($rootScope.token == null || $rootScope.token == undefined) {
            $location.path("/login");
        }

        $scope.accountDetails = null;

        function getBalance() {
            $http({method: 'GET', url: '/api/v1/accounts/balance', headers: {'Authorization': 'token '+$rootScope.token}})
                .then(function(response) {
                    console.log(response.data);
                    $scope.accountDetails = response.data;
                }, function (error)  {
                    if(error.status == 401) {
                        $location.path("/login");
                    }
                });
        }

        getBalance();

        $scope.submit = function() {
            $scope.showErrors = true;

            if($scope.payment_form.$valid != true) {
                return;
            }

            var data = {"user_email" : $scope.email , "amount" : $scope.amount};
            $http({method: 'PUT', url: '/api/v1/accounts/balance_transfer', data: data, headers: {'Authorization': 'token '+$rootScope.token}})
                .then(function(response) {
                    $scope.isSuccess = true ;
                    console.log(response.data);
                    $scope.accountDetails = response.data;
                }, function (error)  {
                    $scope.isSuccess = false ;
                    $scope.error_message = error.data.message;
                    if(error.status == 401) {
                        $location.path("/login");
                    }
                });
        }
    }
);
