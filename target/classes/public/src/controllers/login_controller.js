LoginController = function($scope,$timeout) {
	var self = this;
    
    self.scope = $scope;
    self.timeout = $timeout;

    self.initialize();
    self.setupScopeMethods();
    

    
};
LoginController.prototype.initialize = function() {
    var self = this;
    self.scope.username = null;
    self.scope.password = null;

};

LoginController.prototype.setupScopeMethods = function() {
    var self = this;
}
LoginController.prototype.forceUpdateView = function() {
            var self = this;
            self.timeout(function(){
                self.scope.$apply(); 
            });
}
app.controller('LoginController',LoginController);
