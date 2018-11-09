'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams','$location',
      function ($rootScope,   $state,   $stateParams,$location) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;

          $rootScope.$on('$locationChangeStart', locationChangeStart);

          function locationChangeStart(event) {
              if($location.url()=="/access/signin"||$location.url()=="/access/signup") {
                  document.body.style.backgroundImage = "url('img/bg1.jpg')";
              }else{
                  document.body.style.backgroundImage = "";
              }

          }
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider',
      function ($stateProvider,   $urlRouterProvider) {

          $urlRouterProvider
              .otherwise('/access/signin');

          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '/app',
                  templateUrl: 'tpl/app.html',
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load([
                                  'tpl/blocks/nav.js'+getCacheTime(),
                                  'tpl/blocks/header_nav.js'+getCacheTime(),
                                  'tpl/alert/alert.js'+getCacheTime(),
                                  'tpl/alert/confirm.js'+getCacheTime()]);
                          }]
                  }
              })
              .state('app.dashboard-v1', {
                  url: '/dashboard-v1',
                  templateUrl: 'tpl/test.html',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/chart.js']);
                    }]
                  }
              })
              .state('app.system_sys_user_list', {
                  url: '/system_sys_user_list',
                  templateUrl: 'tpl/system/sys_user/sys_user_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/sys_user/sys_user_list.js'+getCacheTime(),
                                  'tpl/system/sys_user/sys_user_save.js'+getCacheTime(),
                                  'tpl/system/sys_user/sys_user_modify.js'+getCacheTime(),
                                  'tpl/system/sys_user/sys_user_detail.js'+getCacheTime(),
                                  'tpl/system/sys_user/sys_user_modify_pwd.js'+getCacheTime(),
                                  'tpl/system/sys_user/sys_user_list_select.js'+getCacheTime()
                              ]);
                          }]
                  }
              })
              .state('app.system_sys_role_list', {
                  url: '/system_sys_role_list',
                  templateUrl: 'tpl/system/sys_role/sys_role_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/sys_role/sys_role_list.js?datetime='+getTimestamp(),
                                  'tpl/system/sys_role/sys_role_save.js'+getCacheTime(),
                                  'tpl/system/sys_role/sys_role_modify.js'+getCacheTime(),
                                  'tpl/system/sys_role/sys_role_detail.js'+getCacheTime()]);
                          }]
                  }
              })
              .state('app.system_sys_resource_list', {
                  url: '/system_sys_resource_list',
                  templateUrl: 'tpl/system/sys_resource/sys_resource_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/sys_resource/sys_resource_list.js'+getCacheTime(),
                                  'tpl/system/sys_resource/sys_resource_save.js'+getCacheTime(),
                                  'tpl/system/sys_resource/sys_resource_modify.js'+getCacheTime(),
                                  'tpl/system/sys_resource/sys_resource_detail.js'+getCacheTime()]);
                          }]
                  }
              })
              .state('app.recovery_user_list', {
                  url: '/recovery_user_list',
                  templateUrl: 'tpl/system/recovery_company/recovery_user_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/recovery_company/recovery_user_list.js'+getCacheTime(),
                                  'tpl/system/recovery_company/create_recovery_user.js'+getCacheTime(),
                              ]);
                          }]
                  }
              })
              .state('app.lease_user_list', {
                  url: '/lease_user_list',
                  templateUrl: 'tpl/system/lease_company/lease_user_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/lease_company/lease_user_list.js'+getCacheTime(),
                                  'tpl/system/lease_company/create_lease_user.js'+getCacheTime(),
                              ]);
                          }]
                  }
              })
              .state('app.authorization_list', {
                  url: '/authorization_list',
                  templateUrl: 'tpl/system/authorization/authorization_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/authorization/authorization_list.js'+getCacheTime(),
                                  'tpl/system/authorization/authorization_operate.js'+getCacheTime(),
                                  'tpl/system/authorization/authorization_refuse.js'+getCacheTime(),
                                  'tpl/system/authorization/authorization_delay.js'+getCacheTime(),
                              ]);
                          }]
                  }
              })
              .state('app.system_user_report', {
                  url: '/system_user_report',
                  templateUrl: 'tpl/system/system_user_report/system_user_report.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/system_user_report/system_user_report.js'+getCacheTime()
                              ]);
                          }]
                  }
              })
              .state('app.clue_info_report', {
                  url: '/clue_info_report',
                  templateUrl: 'tpl/system/clue_info_report/clue_info_report.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/clue_info_report/clue_info_report.js'+getCacheTime()
                              ]);
                          }]
                  }
              })
              //新闻发布页面
              .state("app.newsPublish", {
                  url: "/newsPublish",
                  templateUrl: "tpl/information/news_publish/news_publish.html",
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function ($ocLazyLoad) {
                              return $ocLazyLoad.load(["toaster", "js/directives/fileModel.js" + getCacheTime(),
                                  "tpl/information/news_publish/news_publish.js" + getCacheTime()]);
                          }]
                  }
              })
              //分享页面
              .state("access.share", {
                  url: "/share",
                  templateUrl: "tpl/appuser/share/share.html",
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function ($ocLazyLoad) {
                              return $ocLazyLoad.load(["tpl/appuser/share/share.js" + getCacheTime()]);
                          }]
                  }
              })
              .state('app.system_sys_organization_property_list', {
                  url: '/system_sys_organization_property_list',
                  templateUrl: 'tpl/system/sys_organization_property/sys_organization_property_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/sys_organization_property/sys_organization_property_list.js'+getCacheTime(),
                                  'tpl/system/sys_organization_property/sys_organization_property_save.js'+getCacheTime(),
                                  'tpl/system/sys_organization_property/sys_organization_property_modify.js'+getCacheTime(),
                                  'tpl/system/sys_organization_property/sys_organization_property_detail.js'+getCacheTime()]);
                          }]
                  }
              })

              .state('app.system_sys_data_dictionary_list', {
                  url: '/system_sys_data_dictionary_list',
                  templateUrl: 'tpl/system/sys_data_dictionary/sys_data_dictionary_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/sys_data_dictionary/sys_data_dictionary_list.js'+getCacheTime(),
                                  'tpl/system/sys_data_dictionary/sys_data_dictionary_save.js'+getCacheTime(),
                                  'tpl/system/sys_data_dictionary/sys_data_dictionary_modify.js'+getCacheTime(),
                                  'tpl/system/sys_data_dictionary/sys_data_dictionary_detail.js'+getCacheTime(),
                                  'tpl/system/sys_data_dictionary/sys_data_dictionary_list_select.js'+getCacheTime(),
                              ]);
                          }]
                  }
              })


              .state('app.system_sys_organization_list', {
                  url: '/system_sys_organization_list',
                  templateUrl: 'tpl/system/sys_organization/sys_organization_list.html'+getCacheTime(),
                  resolve: {
                      deps: ['$ocLazyLoad',
                          function( $ocLazyLoad ){
                              return $ocLazyLoad.load(['toaster','tpl/system/sys_organization/sys_organization_list.js'+getCacheTime(),
                                  'tpl/system/sys_organization/sys_organization_save.js'+getCacheTime(),
                                  'tpl/system/sys_organization/sys_organization_modify.js'+getCacheTime(),
                                  'tpl/system/sys_organization/sys_organization_detail.js'+getCacheTime(),
                                  'tpl/system/sys_organization/sys_organization_list_select.js'+getCacheTime(),
                                  'tpl/system/sys_organization_property/sys_organization_property_list_select.js'+getCacheTime(),
                                  'tpl/system/sys_organization/sys_organization_list_manage.js'+getCacheTime()
                              ]);
                          }]
                  }
              })



              .state('access', {
                  url: '/access',
                  template: '<div ui-view class="fade-in-right-big smooth"></div>'
              })
              .state('access.signin', {
                  url: '/signin',
                  templateUrl: 'tpl/page_signin.html'+getCacheTime(),
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['tpl/page_signin.js'+getCacheTime()] );
                      }]
                  }
              })
              .state('access.signup', {
                  url: '/signup',
                  templateUrl: 'tpl/page_signup.html',
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['tpl/page_signup.js'+getCacheTime()] );
                      }]
                  }
              })
      }
    ]
  ).config(['$httpProvider', function($httpProvider) {
    //Handle 401 Error
    $httpProvider.interceptors.push(function($q, $injector) {
        return {
            response: function(response){
                return response || $q.when(response);
            },
            responseError: function(rejection){
                var currentUrl = $injector.get('$location').url();
                if(rejection.status === 401){
                    var state = $injector.get('$state');
                    state.go("access.signin");
                }
                return $q.reject(rejection);
            }
        };
    });
}]);