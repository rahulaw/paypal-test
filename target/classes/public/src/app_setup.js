app = angular.module('paypalDemoApplication' , ["ngRoute"]);
app.config(function($httpProvider) {

        // Enable CORS
        $httpProvider.defaults.useXDomain = true;
        delete $httpProvider.defaults.headers.common['X-Requested-With'];

  });
