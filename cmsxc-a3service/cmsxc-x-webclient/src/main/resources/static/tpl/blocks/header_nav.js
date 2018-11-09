/**
 * Created by qiaohao on 2018/1/22.
 */
'use strict';

app.controller('headerNavController', ['$scope', '$http','$localStorage','$filter', '$state', '$window','$location', '$rootScope', function ($scope, $http, $localStorage, $filter, $state, $window, $location, $rootScope) {

    if($localStorage.user == undefined || $localStorage.user == null){
        $state.go("access.signin");
    }

    $scope.user = $localStorage.user;
    $scope.roleName = '';
    var userRole = $scope.user.userRole;
    if(userRole != null && userRole != ''){
        $http.get('sys_role/findSysRoleById?id='+userRole).success(function(result){
            $scope.roleName = result.data.roleName;
        })
    }
    // var resources = $localStorage.user.resources;
    // var firstMenu = $filter('filter')(resources, function(data){return data.parentId === null});
    // $scope.menus = [];
    //
    // for(var i in firstMenu){
    //
    //     var parentMenu = getChildren(firstMenu[i]);
    //
    //
    //     var colLength = 5;
    //     var groupLengthKey = 'group';
    //     //计算二级菜单总数算出5列每列放入的二级菜单
    //     var twoMenuLength = parentMenu.children.length;
    //     //每列可以放入多少个二级菜单
    //     var twoColLength = 0;
    //     //余数
    //     var surplus = twoMenuLength % colLength;
    //     if(surplus == 0)
    //         twoColLength = twoMenuLength / colLength;
    //     else{
    //         if(twoMenuLength < colLength)
    //             twoColLength =  parseInt(twoMenuLength / colLength) + 1;
    //         else
    //             twoColLength = parseInt(twoMenuLength / colLength);
    //     }
    //
    //     //计算每列应该放入多少内容
    //     var groupLength = {};
    //     if(twoMenuLength > colLength && surplus != 0 ){
    //         var tmpSurplus = surplus;
    //         for(var index = 0 ; index <colLength; index++ ){
    //             if(tmpSurplus > 0){
    //                 groupLength[groupLengthKey+index] = twoColLength + 1;
    //                 tmpSurplus -- ;
    //             }else{
    //                 groupLength[groupLengthKey+index] = twoColLength ;
    //             }
    //         }
    //     }
    //
    //     // k 代表  以 5个一组 分组的坐标
    //     var k = 0;
    //
    //
    //     parentMenu.twoChildren = [];
    //
    //     for(var j in parentMenu.children){
    //
    //         if(isUndefinedNull(parentMenu.twoChildren[k]))
    //             parentMenu.twoChildren[k] = [];
    //         parentMenu.twoChildren[k].push(parentMenu.children[j]);
    //
    //         if(groupLength[groupLengthKey+k] == parentMenu.twoChildren[k].length ){
    //             //进入下一组
    //             k ++ ;
    //         }
    //
    //     }
    //     $scope.menus.push(parentMenu);
    // }
    // function getChildren(parent){
    //     parent.children = [];
    //     var children = $filter('filter')(resources, function(data){return data.parentId === parent.id});
    //     if(children.length != 0){
    //         for(var i in children){
    //             parent.children.push(getChildren(children[i]));
    //         }
    //     }
    //     return parent;
    // }




}])
;