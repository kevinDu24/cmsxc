'use strict';

/* Controllers */
  // signin controller
app.controller('SigninFormController', ['$scope','$rootScope', '$http', '$state','$localStorage', function($scope,$rootScope, $http, $state,$localStorage) {
    $scope.user = {};
    $scope.authError = null;

    $scope.close = function(){
        $scope.authError = null;
    }

    $scope.login = function() {
      $scope.authError = null;
      // Try to login
      $http.post('oauth2/get_token', $scope.user)
      .then(function(response) {
          var code = response.data.code;
          var access_token = response.data.data.access_token;
          if(code == Response.successCode && isNotUndefined(access_token) && isNotNull(access_token)){


              document.cookie="access-user="+access_token;

              $http.get('sys_user/findSysUserDetail')
                  .then(function(response) {
                      var user = response.data.data;
                      var menus = null;
                      if(user != null) {
                          $localStorage.user = user;
                          menus = user.resources;
                      }
                      if(menus == null || menus.length <=0 ) {
                          $scope.authError = '没有权限访问该系统,请联系管理员';
                      }
                      else{

                          var firstMenu = '';
                          var secondMenu = '';
                          for(var i in menus){
                              if(menus[i].parentId == null){
                                  firstMenu = menus[i];
                                  break;
                              }
                          }
                          for(var i in menus){
                              if(menus[i].parentId == firstMenu.id){
                                  secondMenu = menus[i];
                                  break;
                              }
                          }
                          $rootScope.pageName = secondMenu.description;
                          $state.go(secondMenu.res);
                      }
                  }, function(x) {


               });


          }else{
              $scope.authError = '用户名或密码错误';
          }

      }, function(x) {
        $scope.authError = '用户名或密码错误';
      });
    };
  }])
;