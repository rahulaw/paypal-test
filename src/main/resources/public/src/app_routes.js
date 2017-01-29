// configure our routes
app.config(function($routeProvider) {
    $routeProvider
    // route for the home page
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
        .otherwise({
            redirectTo: '/login'
        });
});


