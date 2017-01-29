app.config(function($routeProvider) {
    $routeProvider
        .when('/login', {
            templateUrl : '/templates/login.html',
            controller  : 'LoginController'
        })
        .when('/signup', {
            templateUrl : '/templates/signup.html',
            controller  : 'SignupController'
        })
        .when('/home', {
            templateUrl : '/templates/home.html',
            controller  : 'HomeController'
        })
        .when('/payment', {
            templateUrl : '/templates/payment.html',
            controller  : 'PaymentController'
        })
        .when('/logout', {
            templateUrl : '/templates/logout.html',
            controller  : 'LogoutController'
        })
        .when('/transactions', {
            templateUrl : '/templates/transactions.html',
            controller  : 'TransactionController'
        })
        .when('/admin', {
            templateUrl : '/templates/admin.html',
            controller  : 'AdminController'
        })
        .when('/admin_home', {
            templateUrl : '/templates/admin_home.html',
            controller  : 'AdminHomeController'
        })
        .otherwise({
            redirectTo: '/login'
        });
});


