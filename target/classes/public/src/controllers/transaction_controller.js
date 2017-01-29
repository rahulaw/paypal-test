app.controller('TransactionController',function($scope,$rootScope,$timeout,$http,$location) {

        if ($rootScope.token == null || $rootScope.token == undefined) {
            $location.path("/login");
        }

        $scope.transactionDetails = null;

        function getTransactions() {
            $http({method: 'GET', url: '/api/v1/accounts/transactions', headers: {'Authorization': 'token ' + $rootScope.token}})
                .then(function (response) {
                    console.log(response.data);
                    $scope.transactionDetails = response.data.transactionHistories;
                }, function (error) {

                });
        }

        getTransactions();
    }

);

